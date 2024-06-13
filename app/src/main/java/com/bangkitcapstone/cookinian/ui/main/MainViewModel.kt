package com.bangkitcapstone.cookinian.ui.main

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

    private val _savedRecipe = MutableLiveData<Event<Result<RegisterResponse>>>()
    val savedRecipe : LiveData<Event<Result<RegisterResponse>>> = _savedRecipe

    init {
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

    fun savedRecipe(userId: String, key: String, title: String, thumb: String, times: String, difficulty: String) {
        viewModelScope.launch {
            try {
                _savedRecipe.value = Event(Result.Loading)
                val response = repository.saveRecipe(
                    userId,
                    key,
                    title,
                    thumb,
                    times,
                    difficulty
                )
                if (!response.error) {
                    _savedRecipe.value = Event(Result.Success(response))
                } else {
                    _savedRecipe.value = Event(Result.Error(response.message))
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val message = Gson().fromJson(error, RegisterResponse::class.java)
                _savedRecipe.value = Event(Result.Error(message.message))
            }
        }
    }

}