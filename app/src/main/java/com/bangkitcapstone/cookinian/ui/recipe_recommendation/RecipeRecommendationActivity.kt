package com.bangkitcapstone.cookinian.ui.recipe_recommendation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivityRecipeRecommendationBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.article.LoadingStateAdapter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class RecipeRecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeRecommendationBinding

    private val recipeRecommendationViewModel by viewModels<RecipeRecommendationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("ingredients")

        setupToolbar()
        if (query != null) {
            setupRecipesRecommendationRecyclerView(query)
        }

        binding.tvRecommendationRecipe.text = getString(R.string.recipe_recommendation_ingredients, query)
    }

    private fun setupRecipesRecommendationRecyclerView(ingredientsQ: String) {
        val adapter = RecipeRecommendationAdapter()
        binding.rvRecommendationRecipe.layoutManager = LinearLayoutManager(this)
        binding.rvRecommendationRecipe.isNestedScrollingEnabled = false
        binding.rvRecommendationRecipe.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChanged { old, new ->
                    old.mediator?.prepend?.endOfPaginationReached == new.mediator?.prepend?.endOfPaginationReached
                }
                .filter {
                    it.refresh is androidx.paging.LoadState.NotLoading
                            && it.prepend.endOfPaginationReached
                            && !it.append.endOfPaginationReached
                }
                .collect {
                    binding.rvRecommendationRecipe.scrollToPosition(0)
                }
        }

        recipeRecommendationViewModel.getRecipeRecommendationWithPaging(ingredientsQ).observe(this) {
            adapter.submitData(lifecycle, it)
        }
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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = getString(R.string.recipe_recommendation_title)
        }
    }
}