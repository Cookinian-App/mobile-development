package com.bangkitcapstone.cookinian.ui.search_category

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivitySearchCategoryBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.article.ArticleListAdapter
import com.bangkitcapstone.cookinian.ui.article.LoadingStateAdapter
import com.bangkitcapstone.cookinian.ui.article.RecipeListAdapter

class SearchCategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchCategoryBinding

    private val searchCategoryViewModel by viewModels<SearchCategoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataCategory = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")
        } else {
            null
        }

        if(dataCategory == "inspirasi-dapur" || dataCategory == "makanan-gaya-hidup" || dataCategory == "tips-masak") {
            setupArticleListRecyclerView(dataCategory)
        } else {
            setupRecipeListRecyclerView(dataCategory)
        }

        setupToolbar(dataCategory?.let { capitalizeWords(it) })
    }

    private fun setupToolbar(dataCategory: String?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = dataCategory
        }
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

        searchCategoryViewModel.getRecipes(dataCategory).observe(this) {
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

        searchCategoryViewModel.getArticles(dataCategory).observe(this) {
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
}