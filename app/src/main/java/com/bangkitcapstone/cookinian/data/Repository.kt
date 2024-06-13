package com.bangkitcapstone.cookinian.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkitcapstone.cookinian.data.api.retrofit.ApiService
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
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
    // API Auth
    fun getSession(): Flow<UserModel> = userPreference.getSession()
    suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)
    suspend fun editSession(newName: String) = userPreference.editSession(newName)
    suspend fun clearSession() = userPreference.clearSession()
    suspend fun register(name: String, email: String, password: String) = authApiService.register(name, email, password)
    suspend fun login(email: String, password: String) = authApiService.login(email, password)
    suspend fun editProfile(email: String, newName: String) = authApiService.editProfile(email, newName)
    suspend fun changePassword(email: String, currentPassword: String, newPassword: String, confirmNewPassword: String) = authApiService.changePassword(email, currentPassword, newPassword, confirmNewPassword)

    // Recipe API
    suspend fun getCategory() = recipeApiService.getCategory()
    suspend fun getRecipes() = recipeApiService.getRecipes()
    suspend fun getDetailRecipe(key: String) = recipeApiService.getDetailRecipe(key)
    @OptIn(ExperimentalPagingApi::class)
    fun getRecipesWithPaging(searchQuery: String? = null, category: String? = null): LiveData<PagingData<RecipeItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            remoteMediator =  RecipeRemoteMediator(
                database,
                recipeApiService,
                searchQuery,
                category
            ),
            pagingSourceFactory = {
                database.recipeDao().getRecipes()
            }
        ).liveData
    }
    suspend fun getSavedRecipe(userId: String) = authApiService.getSavedRecipe(userId)
    suspend fun saveRecipe(userId: String, key: String, title: String, thumb: String, times: String, difficulty: String) = authApiService.saveRecipe(userId, key, title, thumb, times, difficulty)

    // Article API
    suspend fun getArticleCategory() = recipeApiService.getArticleCategory()
    @OptIn(ExperimentalPagingApi::class)
    fun getArticlesWithPaging(category: String? = null): LiveData<PagingData<ArticleItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            remoteMediator =  ArticleRemoteMediator(
                database,
                recipeApiService,
                category
            ),
            pagingSourceFactory = {
                database.articleDao().getArticles()
            }
        ).liveData
    }
    suspend fun getArticleDetail(tag: String, key: String) = recipeApiService.getArticleDetail(tag, key)

    suspend fun saveThemeMode(themeMode: String) {
        userPreference.saveThemeMode(themeMode)
    }
    fun getThemeMode(): Flow<String> {
        return userPreference.getThemeMode()
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