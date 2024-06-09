package com.bangkitcapstone.cookinian.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.response.ArticleItem
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories : LiveData<List<CategoryItem>> = _categories

    private val _articles = MutableLiveData<List<ArticleItem>>()
    val articles : LiveData<List<ArticleItem>> = _articles

    init {
        getCategories()
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            val result = repository.getArticles().results
            _articles.value = result
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            val result = repository.getCategory().results
            _categories.value = result
        }
    }

}