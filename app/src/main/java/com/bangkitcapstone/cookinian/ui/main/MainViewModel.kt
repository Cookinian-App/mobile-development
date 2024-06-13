package com.bangkitcapstone.cookinian.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import com.bangkitcapstone.cookinian.helper.Event
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories : LiveData<List<CategoryItem>> = _categories

    private val _recipes = MutableLiveData<List<RecipeItem>>()
    val recipes : LiveData<List<RecipeItem>> = _recipes

    init {
        getSavedRecipe()
        getCategories()
        getRecipes()
    }

    fun getSession() = repository.getSession().asLiveData()

    private fun getCategories() {
        viewModelScope.launch {
            val result = repository.getCategory().results
            _categories.value = result
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            val result = repository.getRecipes().results
            _recipes.value = result
        }
    }

    private fun getSavedRecipe() {
        viewModelScope.launch {
            repository.getSession().collect {
                repository.saveRecipeFromSavedRecipeApi(it.userId)
            }
        }
    }

}