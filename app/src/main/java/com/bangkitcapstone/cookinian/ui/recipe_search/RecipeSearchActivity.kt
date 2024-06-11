package com.bangkitcapstone.cookinian.ui.recipe_search

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivityRecipeSearchBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.article.LoadingStateAdapter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class RecipeSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeSearchBinding

    private val recipeSearchViewModel by viewModels<RecipeSearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("searchQuery")

        setupToolbar()
        setupRecipeListRecyclerView(query)

        val searchView = binding.searchView
        searchView.setQuery(query, false)

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    setupRecipeListRecyclerView(query)
                }
                closeKeyboard()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupRecipeListRecyclerView(searchQuery: String? = null) {
        val adapter = RecipeListAdapter()
        binding.rvSearchRecipe.layoutManager = LinearLayoutManager(this)
        binding.rvSearchRecipe.isNestedScrollingEnabled = false
        binding.rvSearchRecipe.adapter = adapter.withLoadStateFooter(
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
                    binding.rvSearchRecipe.scrollToPosition(0)
                }
        }

        recipeSearchViewModel.getRecipesWithPaging(searchQuery).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
        binding.searchView.clearFocus()
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
        }
    }
}