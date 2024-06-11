package com.bangkitcapstone.cookinian.ui.category_search

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivityCategorySearchBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.article.ArticleListAdapter
import com.bangkitcapstone.cookinian.ui.article.LoadingStateAdapter
import com.bangkitcapstone.cookinian.ui.recipe_search.RecipeListAdapter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class CategorySearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategorySearchBinding

    private val categorySearchViewModel by viewModels<CategorySearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataCategory = intent.getStringExtra("category")

        if(dataCategory == "inspirasi-dapur" || dataCategory == "makanan-gaya-hidup" || dataCategory == "tips-masak") {
            setupArticleListRecyclerView(dataCategory)
        } else {
            setupRecipeListRecyclerView(dataCategory)
        }

        setupToolbar(dataCategory?.let { capitalizeWords(it) })
    }

    private fun setupRecipeListRecyclerView(dataCategory: String? = null) {
        val adapter = RecipeListAdapter()
        binding.rvSearchCategory.layoutManager = LinearLayoutManager(this)
        binding.rvSearchCategory.isNestedScrollingEnabled = false
        binding.rvSearchCategory.adapter = adapter.withLoadStateFooter(
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
                    binding.rvSearchCategory.scrollToPosition(0)
                }
        }

        categorySearchViewModel.getRecipes(dataCategory).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupArticleListRecyclerView(dataCategory: String? = null) {
        val adapter = ArticleListAdapter()
        binding.rvSearchCategory.layoutManager = LinearLayoutManager(this)
        binding.rvSearchCategory.isNestedScrollingEnabled = false
        binding.rvSearchCategory.adapter = adapter.withLoadStateFooter(
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
                    binding.rvSearchCategory.scrollToPosition(0)
                }
        }

        categorySearchViewModel.getArticles(dataCategory).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupToolbar(dataCategory: String?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = dataCategory
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
}