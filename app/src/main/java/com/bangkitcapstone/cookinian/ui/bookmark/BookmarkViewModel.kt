package com.bangkitcapstone.cookinian.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: Repository): ViewModel() {
    private val _savedRecipe = MutableLiveData<List<RecipeItem>>()
    val savedRecipe: LiveData<List<RecipeItem>> = _savedRecipe

    init {
        getSavedRecipe()
    }

    private fun getSavedRecipe() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _savedRecipe.value = repository.getSavedRecipe(user.userId).recipes
            }
        }
    }
}