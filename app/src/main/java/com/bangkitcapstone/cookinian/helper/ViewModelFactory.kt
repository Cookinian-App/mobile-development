package com.bangkitcapstone.cookinian.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.di.Injection
import com.bangkitcapstone.cookinian.ui.login.LoginViewModel
import com.bangkitcapstone.cookinian.ui.main.MainViewModel
import com.bangkitcapstone.cookinian.ui.profile.ProfileViewModel
import com.bangkitcapstone.cookinian.ui.recipe.RecipeViewModel
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailViewModel
import com.bangkitcapstone.cookinian.ui.register.RegisterViewModel
import com.bangkitcapstone.cookinian.ui.search_category.SearchCategoryViewModel
import com.bangkitcapstone.cookinian.ui.search_recipe.SearchRecipeViewModel
import com.bangkitcapstone.cookinian.ui.splash.SplashViewModel

class ViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            return RecipeDetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SearchRecipeViewModel::class.java)) {
            return SearchRecipeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SearchCategoryViewModel::class.java)) {
            return SearchCategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}