package com.bangkitcapstone.cookinian.ui.saved_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import kotlinx.coroutines.launch

class SavedRecipeViewModel(private val repository: Repository): ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
    fun getSavedRecipe() = repository.getBookmarkedRecipe()

    fun deleteAllSavedRecipes() {
        viewModelScope.launch {
            repository.deleteAllSavedRecipeFromLocal()
            repository.getSession().collect {
                repository.deleteAllSavedRecipeFromApi(it.userId)
            }
        }
    }
}