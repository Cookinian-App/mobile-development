package com.bangkitcapstone.cookinian.ui.recipe_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RecipeDetailViewModel(private val repository: Repository): ViewModel() {
    private val _recipeDetail = MutableLiveData<RecipeDetailResults>()
    val recipeDetail: LiveData<RecipeDetailResults> = _recipeDetail

    private val _savedRecipe = MutableLiveData<Result<RegisterResponse>>()
    val savedRecipe: LiveData<Result<RegisterResponse>> = _savedRecipe

    fun getSession() = repository.getSession().asLiveData()

    fun getRecipeDetail(key: String) {
        viewModelScope.launch {
            val result = repository.getDetailRecipe(key).results
            _recipeDetail.value = result
        }
    }

    fun savedRecipe(userId: String, key: String, title: String, thumb: String, times: String, difficulty: String) {
        viewModelScope.launch {
            try {
                _savedRecipe.value = Result.Loading
                val response = repository.saveRecipe(
                    userId,
                    key,
                    title,
                    thumb,
                    times,
                    difficulty
                )
                if (!response.error) {
                    _savedRecipe.value = Result.Success(response)
                } else {
                    _savedRecipe.value = Result.Error(response.message)
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val message = Gson().fromJson(error, RegisterResponse::class.java)
                _savedRecipe.value = Result.Error(message.message)
            }
        }
    }
}