package com.bangkitcapstone.cookinian.ui.main

import androidx.lifecycle.*
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.helper.handleHttpException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<Result<List<CategoryItem>>>()
    val categories : LiveData<Result<List<CategoryItem>>> = _categories

    private val _recipes = MutableLiveData<Result<List<RecipeItem>>>()
    val recipes : LiveData<Result<List<RecipeItem>>> = _recipes

    private val _savedRecipe = MutableLiveData<Result<Boolean>>()
    val savedRecipe : LiveData<Result<Boolean>> = _savedRecipe

    // State for checking if data is fetched
    private var isCategoriesFetched = false
    private var isRecipesFetched = false
    private var isSavedRecipeFetched = false

    fun getSession() = repository.getSession().asLiveData()

    fun getCategories(forceFetch: Boolean = false) {
        if (!forceFetch && isCategoriesFetched) return
        viewModelScope.launch {
            try {
                _categories.value = Result.Loading
                val result = repository.getCategory().results
                _categories.value = Result.Success(result)
                isCategoriesFetched = true
            } catch (e: HttpException) {
                handleHttpException(e, _categories)
            } catch (e: UnknownHostException) {
                _categories.value = Result.Error("Kesalahan jaringan, tidak dapat menghubungkan ke server")
            } catch (e: Exception) {
                _categories.value = e.message?.let { Result.Error(it) }
            }
        }
    }

    fun getRecipes(forceFetch: Boolean = false) {
        if (!forceFetch && isRecipesFetched) return
        viewModelScope.launch {
            try {
                _recipes.value = Result.Loading
                val result = repository.getRecipes().results
                _recipes.value = Result.Success(result)
                isRecipesFetched = true
            } catch (e: HttpException) {
                handleHttpException(e, _recipes)
            } catch (e: UnknownHostException) {
                _recipes.value = Result.Error("Kesalahan jaringan, tidak dapat menghubungkan ke server")
            } catch (e: Exception) {
                _recipes.value = e.message?.let { Result.Error(it) }
            }
        }
    }

    fun getSavedRecipe(forceFetch: Boolean = false) {
        if (!forceFetch && isSavedRecipeFetched) return
        viewModelScope.launch {
            repository.getSession().collect {
                try {
                    _savedRecipe.value = Result.Loading
                    repository.saveRecipeFromSavedRecipeApi(it.userId)
                    _savedRecipe.value = Result.Success(true)
                    isSavedRecipeFetched = true
                } catch (e: HttpException) {
                    handleHttpException(e, _savedRecipe)
                } catch (e: UnknownHostException) {
                    _savedRecipe.value = Result.Error("Kesalahan jaringan, tidak dapat menghubungkan ke server")
                } catch (e: Exception) {
                    _savedRecipe.value = e.message?.let { err -> Result.Error(err) }
                }
            }
        }
    }
}
