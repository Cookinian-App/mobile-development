package com.bangkitcapstone.cookinian.ui.recipe_recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.RecipesRecommendationItem

class RecipeRecommendationViewModel(private val repository: Repository) : ViewModel() {
    fun getRecipeRecommendationWithPaging(ingredientsQ: String): LiveData<PagingData<RecipesRecommendationItem>> {
        return repository.getRecipesRecommendationWithPaging(ingredientsQ).cachedIn(viewModelScope)
    }
}