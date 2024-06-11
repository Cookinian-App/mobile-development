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

    var currentCategory: String? = null
    var selectedItem: Int = -1

    init {
        getArticleCategories()
    }

    private fun getArticleCategories() {
        viewModelScope.launch {
            val result = repository.getArticleCategory().results
            if (result.isNotEmpty()) {
                _categories.value = result
                if (selectedItem == -1) {
                    currentCategory = result[0].key
                    selectedItem = 0
                }
            }
        }
    }

    fun getArticleWithPaging(category: String? = null): LiveData<PagingData<ArticleItem>> {
        return repository.getArticlesWithPaging(category).cachedIn(viewModelScope)
    }
}
