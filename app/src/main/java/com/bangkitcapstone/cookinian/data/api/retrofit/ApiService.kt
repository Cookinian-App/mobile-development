package com.bangkitcapstone.cookinian.data.api.retrofit

import com.bangkitcapstone.cookinian.data.api.response.ArticleResponse
import com.bangkitcapstone.cookinian.data.api.response.CategoryResponse
import com.bangkitcapstone.cookinian.data.api.response.LoginResponse
import com.bangkitcapstone.cookinian.data.api.response.RecipeResponse
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("category/recipes")
    suspend fun getCategory(): CategoryResponse

    @GET("articles/new")
    suspend fun getArticles(): ArticleResponse

    @GET("recipes")
    suspend fun getRecipes(): RecipeResponse

    @GET("recipes/{page}")
    suspend fun getRecipesWithPaging(
        @Path("page") page: Int
    ): RecipeResponse
}