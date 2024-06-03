package com.bangkitcapstone.cookinian.ui.recipe_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import kotlinx.coroutines.launch

class RecipeDetailViewModel(private val repository: Repository): ViewModel() {
    private val _recipeDetail = MutableLiveData<RecipeDetailResults>()
    val recipeDetail: LiveData<RecipeDetailResults> = _recipeDetail

    fun getRecipeDetail(key: String) {
        viewModelScope.launch {
            val result = repository.getDetailRecipe(key).results
            _recipeDetail.value = result
        }
    }
}