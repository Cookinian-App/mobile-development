package com.bangkitcapstone.cookinian.data.api.retrofit

import com.bangkitcapstone.cookinian.data.api.response.ArticleDetailResponse
import com.bangkitcapstone.cookinian.data.api.response.ArticleResponse
import com.bangkitcapstone.cookinian.data.api.response.CategoryResponse
import com.bangkitcapstone.cookinian.data.api.response.LoginResponse
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResponse
import com.bangkitcapstone.cookinian.data.api.response.RecipeResponse
import com.bangkitcapstone.cookinian.data.api.response.RecipesRecommendationResponse
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.data.api.response.SavedRecipeResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("change-name")
    suspend fun editProfile(
        @Field("email") email: String,
        @Field("newName") newName: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changePassword(
        @Field("email") email: String,
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmNewPassword") confirmNewPassword: String
    ): RegisterResponse

    @GET("category/recipes")
    suspend fun getCategory(): CategoryResponse

    @GET("recipes")
    suspend fun getRecipes(): RecipeResponse

    @GET("recipes/page/{page}")
    suspend fun getRecipesWithPaging(
        @Path("page") page: Int,
        @Query("s") searchQuery: String? = null,
        @Query("category") category: String? = null
    ): RecipeResponse

    @GET("api/recipes/{page}")
    suspend fun getRecommendationRecipeWithPaging(
        @Path("page") key: Int,
        @Query("bahan") ingredientsQ: String
    ): RecipesRecommendationResponse

    @GET("recipe/{key}")
    suspend fun getDetailRecipe(
        @Path("key") key: String
    ): RecipeDetailResponse

    @GET("api/save/get/{uid}")
    suspend fun getSavedRecipe(
        @Path("uid") userId: String
    ): SavedRecipeResponse

    @FormUrlEncoded
    @POST("api/save/post")
    suspend fun saveRecipe(
        @Field("uid") userId: String,
        @Field("key") key: String,
        @Field("title") title: String,
        @Field("thumb") thumb: String,
        @Field("times") times: String,
        @Field("difficulty") difficulty: String
    ): RegisterResponse

    @DELETE("api/save/unsave/{uid},{key}")
    suspend fun unSaveRecipe(
        @Path("uid") userId: String,
        @Path("key") key: String
    ): RegisterResponse

    @DELETE("api/save/delete-all/{uid}")
    suspend fun deleteAllSavedRecipe(
        @Path("uid") userId: String,
    ): RegisterResponse

    @GET("category/article")
    suspend fun getArticleCategory(): CategoryResponse

    @GET("articles/page/{page}")
    suspend fun getArticlesWithPaging(
        @Path("page") page: Int,
        @Query("category") category: String? = null
    ): ArticleResponse

    @GET("article/{tag}/{key}")
    suspend fun getArticleDetail(
        @Path("tag") tag: String,
        @Path("key") key: String
    ): ArticleDetailResponse
}