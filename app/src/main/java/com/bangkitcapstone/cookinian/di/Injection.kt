package com.bangkitcapstone.cookinian.di

import android.content.Context
import com.bangkitcapstone.cookinian.BuildConfig
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.retrofit.ApiConfig
import com.bangkitcapstone.cookinian.data.local.room.CookinianDatabase
import com.bangkitcapstone.cookinian.data.preference.UserPreference
import com.bangkitcapstone.cookinian.data.preference.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val authApiService = ApiConfig.getApiService(userPreference, BuildConfig.AUTH_API_URL)
        val recipeApiService = ApiConfig.getApiService(userPreference, BuildConfig.RECIPE_API_URL)
        val database = CookinianDatabase.getInstance(context)
        return Repository.getInstance(authApiService, recipeApiService, userPreference, database)
    }
}