package com.bangkitcapstone.cookinian.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkitcapstone.cookinian.data.api.retrofit.ApiService
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.data.local.room.CookinianDatabase
import com.bangkitcapstone.cookinian.data.preference.UserModel
import com.bangkitcapstone.cookinian.data.preference.UserPreference
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val authApiService: ApiService,
    private val recipeApiService: ApiService,
    private val userPreference: UserPreference,
    private val database: CookinianDatabase,
){

    fun getSession(): Flow<UserModel> = userPreference.getSession()
    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    suspend fun clearSession() = userPreference.clearSession()
    suspend fun register(name: String, email: String, password: String) = authApiService.register(name, email, password)
    suspend fun login(email: String, password: String) = authApiService.login(email, password)

    suspend fun getCategory() = recipeApiService.getCategory()
    suspend fun getArticles() = recipeApiService.getArticles()
    suspend fun getRecipes() = recipeApiService.getRecipes()
    suspend fun getDetailRecipe(key: String) = recipeApiService.getDetailRecipe(key)

    @OptIn(ExperimentalPagingApi::class)
    fun getRecipesWithPaging(): LiveData<PagingData<RecipeItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = RecipeRemoteMediator(database, recipeApiService),
            pagingSourceFactory = {
                database.recipeDao().getRecipes()
            }
        ).liveData
    }

    companion object {
        fun getInstance(
            authApiService: ApiService,
            recipeApiService: ApiService,
            userPreference: UserPreference,
            database: CookinianDatabase
        ) = Repository(authApiService, recipeApiService, userPreference, database)
    }
}