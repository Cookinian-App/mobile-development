package com.bangkitcapstone.cookinian.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.response.ArticleItem
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories : LiveData<List<CategoryItem>> = _categories

    private val _articles = MutableLiveData<List<ArticleItem>>()
    val articles : LiveData<List<ArticleItem>> = _articles

    private val _recipes = MutableLiveData<List<RecipeItem>>()
    val recipes : LiveData<List<RecipeItem>> = _recipes

    init {
        getCategories()
        getArticles()
        getRecipes()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val result = repository.getCategory().results
            _categories.value = result
        }
    }

    private fun getArticles() {
        viewModelScope.launch {
            val result = repository.getArticles().results
            _articles.value = result
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            val result = repository.getRecipes().results
            _recipes.value = result
        }
    }

}