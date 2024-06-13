package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RecipeDetailViewModel(private val repository: Repository): ViewModel() {
    private val _recipeDetail = MutableLiveData<RecipeDetailResults>()
    val recipeDetail: LiveData<RecipeDetailResults> = _recipeDetail

    fun getSession() = repository.getSession().asLiveData()

    fun getRecipeDetail(key: String) {
        viewModelScope.launch {
            val result = repository.getDetailRecipe(key).results
            _recipeDetail.value = result
        }
    }

    fun saveRecipe(recipe: SavedRecipeEntity) {
        viewModelScope.launch {
            repository.setSavedRecipe(recipe)
            repository.getSession().collect {
                repository.saveRecipeToApi(it.userId, recipe)
            }
        }
    }

    fun deleteRecipe(recipe: SavedRecipeEntity) {
        viewModelScope.launch {
            repository.deleteSavedRecipeFromLocal(recipe)
            repository.getSession().collect {
                repository.deleteSavedRecipeFromApi(it.userId, recipe.key)
            }
        }
    }

    fun checkIsSavedRecipe(key: String): LiveData<Boolean> {
        return repository.isSavedRecipe(key)
    }

}