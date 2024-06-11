package com.bangkitcapstone.cookinian.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>> = _categories

    init {
        getArticleCategories()
    }

    private fun getArticleCategories() {
        viewModelScope.launch {
            val result = repository.getArticleCategory().results
            _categories.value = result
        }
    }
}
