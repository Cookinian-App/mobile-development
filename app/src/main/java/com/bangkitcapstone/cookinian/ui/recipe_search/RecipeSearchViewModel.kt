package com.bangkitcapstone.cookinian.ui.recipe_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem

class RecipeSearchViewModel(private val repository: Repository) : ViewModel() {

    fun getRecipesWithPaging(searchQuery: String? = null): LiveData<PagingData<RecipeItem>> {
        return repository.getRecipesWithPaging(searchQuery).cachedIn(viewModelScope)
    }
}