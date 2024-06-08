package com.bangkitcapstone.cookinian.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem

class RecipeViewModel(private val repository: Repository) : ViewModel() {

    fun getRecipes(category: String? = null): LiveData<PagingData<RecipeItem>> {
        return repository.getRecipesWithPaging(category).cachedIn(viewModelScope)
    }

}