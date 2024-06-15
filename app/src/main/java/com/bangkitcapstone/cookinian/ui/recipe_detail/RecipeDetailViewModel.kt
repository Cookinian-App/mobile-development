package com.bangkitcapstone.cookinian.ui.recipe_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import com.bangkitcapstone.cookinian.helper.Event
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class RecipeDetailViewModel(private val repository: Repository): ViewModel() {
    private val _recipeDetail = MutableLiveData<Result<RecipeDetailResults>>()
    val recipeDetail: LiveData<Result<RecipeDetailResults>> = _recipeDetail

    fun getSession() = repository.getSession().asLiveData()

    fun getRecipeDetail(key: String) {
        viewModelScope.launch {
            _recipeDetail.value = Result.Loading
            try {
                val result = repository.getDetailRecipe(key).results
                _recipeDetail.value = Result.Success(result)
            } catch (e: HttpException) {
                _recipeDetail.value = Result.Error(e.message())
            } catch (e: Exception) {
                _recipeDetail.value = Result.Error("Tidak dapat menghubungkan ke server!")
            } catch (e: UnknownHostException) {
                _recipeDetail.value = Result.Error("Tidak ada koneksi internet, silahkan coba lagi.")
            }
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