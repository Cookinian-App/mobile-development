package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import com.bangkitcapstone.cookinian.databinding.ActivityRecipeDetailBinding
import com.bangkitcapstone.cookinian.helper.Event
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bangkitcapstone.cookinian.helper.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
                        showAlert(this, getString(R.string.recipe_detail), it)
                    }
                }
            }
        }

        binding.llDetailInfoIngredient.setOnClickListener {
            toggleIngredientVisibility()
        }

        binding.llDetailInfoStep.setOnClickListener {
            toggleStepVisibility()
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
                    showToast(this, getString(R.string.save_recipe_delete))
                    true
                }
            } else {
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark)
                binding.toolbar.menu.findItem(R.id.menu_bookmarks).setOnMenuItemClickListener {
                    binding.toolbar.menu.findItem(R.id.menu_bookmarks).setIcon(R.drawable.ic_bookmark_filled)
                    viewModel.saveRecipe(recipe)
                    showToast(this, getString(R.string.save_recipe_success))
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

    private fun toggleIngredientVisibility() {
        if (binding.rvDetailRecipeIngredient.visibility == View.GONE) {
            binding.rvDetailRecipeIngredient.visibility = View.VISIBLE
            binding.icDetailInfoIngredient.setImageResource(R.drawable.ic_up)
        } else {
            binding.rvDetailRecipeIngredient.visibility = View.GONE
            binding.icDetailInfoIngredient.setImageResource(R.drawable.ic_down)
        }
    }

    private fun toggleStepVisibility() {
        if (binding.rvDetailRecipeStep.visibility == View.GONE) {
            binding.rvDetailRecipeStep.visibility = View.VISIBLE
            binding.icDetailInfoStep.setImageResource(R.drawable.ic_up)
        } else {
            binding.rvDetailRecipeStep.visibility = View.GONE
            binding.icDetailInfoStep.setImageResource(R.drawable.ic_down)
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

    private fun handleShareRecipe() {
        val title = binding.tvDetailRecipeTitle.text.toString()
        val thumb = intent.getStringExtra("thumb")!!

        Glide.with(this)
            .asBitmap()
            .load(thumb)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_${title}.png")
                        val fOut = FileOutputStream(file)
                        resource.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                        fOut.flush()
                        fOut.close()

                        val uri = FileProvider.getUriForFile(
                            this@RecipeDetailActivity,
                            "$packageName.fileprovider",
                            file
                        )

                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Lihat Resep ${title} di Aplikasi Cookinian")
                            putExtra(Intent.EXTRA_STREAM, uri)
                            type = "image/*"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(Intent.createChooser(shareIntent, "Bagikan Resep"))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            })
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

            R.id.menu_share -> {
                handleShareRecipe()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}