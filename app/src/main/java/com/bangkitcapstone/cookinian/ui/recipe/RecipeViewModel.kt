package com.bangkitcapstone.cookinian.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem

class RecipeViewModel(private val repository: Repository): ViewModel() {
//    fun getRecipesWithPaging(): LiveData<PagingData<RecipeItem>> = repository.getRecipesWithPaging().cachedIn(viewModelScope)
    val recipe: LiveData<PagingData<RecipeItem>> = repository.getRecipesWithPaging().cachedIn(viewModelScope)
}