package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import com.bangkitcapstone.cookinian.databinding.ActivityRecipeDetailBinding
import com.bangkitcapstone.cookinian.helper.Event
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration


class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding

    private val viewModel by viewModels<RecipeDetailViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")!!
        val thumb = intent.getStringExtra("thumb")!!

        viewModel.getRecipeDetail(key)

        viewModel.recipeDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbRecipeDetail.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbRecipeDetail.visibility = View.GONE
                    setRecipeDetailData(result.data, thumb)
                    checkIsRecipeSaved(
                        key,
                        SavedRecipeEntity(
                            key,
                            result.data.title,
                            thumb,
                            result.data.times,
                            result.data.difficulty,
                            true
                        )
                    )
                }
                is Result.Error -> {
                    Event(result.error).getContentIfNotHandled()?.let {
                        binding.pbRecipeDetail.visibility = View.GONE
                        showAlert(this, "Terjadi kesalahan", it)
                    }
                }
            }
        }

        setupToolbar()
    }

    private fun checkIsRecipeSaved(key: String, recipe: SavedRecipeEntity) {
        viewModel.checkIsSavedRecipe(key).observe(this) {isSaved ->
            if (isSaved) {
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark_filled)
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setOnMenuItemClickListener {
                    binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark)
                    viewModel.deleteRecipe(recipe)
                    Toast.makeText(this, "Recipe removed from bookmarks", Toast.LENGTH_SHORT).show()
                    true
                }
            } else {
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark)
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setOnMenuItemClickListener {
                    binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark_filled)
                    viewModel.saveRecipe(recipe)
                    Toast.makeText(this, "Recipe added to bookmarks", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun setRecipeDetailData(recipe: RecipeDetailResults, thumb: String) {
        val serving = intent.getStringExtra("serving")!!
        val calories = intent.getStringExtra("calories")!!

        binding.apply {
            Glide.with(this@RecipeDetailActivity)
                .load(thumb)
                .into(binding.ivDetailRecipeImage)
            tvDetailRecipeTitle.text = recipe.title
            tvDetailRecipeUser.text = recipe.author.user
            tvDetailRecipeDate.text = recipe.author.datePublished
            tvDetailRecipeTimes.text = recipe.times
                .replace("jam", " Jam")
                .replace("mnt", " Mnt")
                .replace("j", " J")
            tvDetailRecipeDifficulty.text = recipe.difficulty
            tvDetailRecipeDescription.text = recipe.desc
            tvDetailRecipeDescription.post { tvDetailRecipeDescription.toggle() }
            tvDetailRecipeServing.text = serving.ifEmpty { "-" }
            tvDetailRecipeCalories.text = calories.ifEmpty { "-" }

            val divider = MaterialDividerItemDecoration(
                this@RecipeDetailActivity,
                LinearLayoutManager.VERTICAL
            )

            rvDetailRecipeIngredient.apply {
                layoutManager = LinearLayoutManager(this@RecipeDetailActivity)
                adapter = RecipeIngredientAdapter(recipe.ingredient)
                addItemDecoration(divider)
            }

            rvDetailRecipeStep.apply {
                layoutManager = LinearLayoutManager(this@RecipeDetailActivity)
                adapter = RecipeStepAdapter(recipe.step)
                addItemDecoration(divider)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}