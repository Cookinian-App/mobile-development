package com.bangkitcapstone.cookinian.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: Repository): ViewModel() {

    fun getSession() = repository.getSession().asLiveData()

    fun getSavedRecipe() = repository.getBookmarkedRecipe()
}