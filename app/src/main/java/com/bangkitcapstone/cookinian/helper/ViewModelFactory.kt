package com.bangkitcapstone.cookinian.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.di.Injection
import com.bangkitcapstone.cookinian.ui.login.LoginViewModel
import com.bangkitcapstone.cookinian.ui.main.MainViewModel
import com.bangkitcapstone.cookinian.ui.profile.ProfileViewModel
import com.bangkitcapstone.cookinian.ui.article.ArticleViewModel
import com.bangkitcapstone.cookinian.ui.article_detail.ArticleDetailViewModel
import com.bangkitcapstone.cookinian.ui.saved_recipe.SavedRecipeViewModel
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailViewModel
import com.bangkitcapstone.cookinian.ui.register.RegisterViewModel
import com.bangkitcapstone.cookinian.ui.category_search.CategorySearchViewModel
import com.bangkitcapstone.cookinian.ui.profile_edit.ProfileEditViewModel
import com.bangkitcapstone.cookinian.ui.profile_edit_pass.ProfileEditPassViewModel
import com.bangkitcapstone.cookinian.ui.recipe_recommendation.RecipeRecommendationViewModel
import com.bangkitcapstone.cookinian.ui.recipe_search.RecipeSearchViewModel
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
        } else if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            return RecipeDetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeSearchViewModel::class.java)) {
            return RecipeSearchViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CategorySearchViewModel::class.java)) {
            return CategorySearchViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
            return ArticleDetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            return ProfileEditViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileEditPassViewModel::class.java)) {
            return ProfileEditPassViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SavedRecipeViewModel::class.java)) {
            return SavedRecipeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RecipeRecommendationViewModel::class.java)) {
            return RecipeRecommendationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}