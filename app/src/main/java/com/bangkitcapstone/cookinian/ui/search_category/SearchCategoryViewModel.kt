package com.bangkitcapstone.cookinian.ui.search_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem

class SearchCategoryViewModel(private val repository: Repository) : ViewModel() {

    fun getRecipes(category: String? = null): LiveData<PagingData<RecipeItem>> {
        return repository.getRecipesWithPaging(category = category).cachedIn(viewModelScope)
    }

    fun getArticles(category: String? = null): LiveData<PagingData<ArticleItem>> {
        return repository.getArticlesWithPaging(category).cachedIn(viewModelScope)
    }
}