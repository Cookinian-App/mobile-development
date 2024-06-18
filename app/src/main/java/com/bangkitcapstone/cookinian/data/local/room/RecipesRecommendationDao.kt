package com.bangkitcapstone.cookinian.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkitcapstone.cookinian.data.local.entity.RecipesRecommendationItem

@Dao
interface RecipesRecommendationDao {
    @Query("SELECT * FROM recipe_recommendation")
    fun getRecipes(): PagingSource<Int, RecipesRecommendationItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipesRecommendationItem>)

    @Query("DELETE FROM recipe_recommendation")
    suspend fun deleteAllRecipe()
}