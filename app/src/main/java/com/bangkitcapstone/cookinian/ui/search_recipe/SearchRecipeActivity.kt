package com.bangkitcapstone.cookinian.ui.search_recipe

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivitySearchRecipeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class SearchRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRecipeBinding

    private val searchRecipeViewModel by viewModels<SearchRecipeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("searchQuery") ?: ""

        setupToolbar()

        val searchView = binding.searchView
        searchView.setQuery(query, false)
        Toast.makeText(this, "Ambil data: $query", Toast.LENGTH_SHORT).show()

        //Kalau ada intent query, tampilkan recyclerview getrecipe dengan parameter (user melakukan search dari home)
        //Kalau tidak ada intent query, tampilkan recyclerview getrecipe keseluruhan

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    Toast.makeText(this@SearchRecipeActivity, "Ambil data: $query", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
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