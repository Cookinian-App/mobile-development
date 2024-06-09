package com.bangkitcapstone.cookinian.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getRecipes(): PagingSource<Int, RecipeItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(story: List<RecipeItem>)

    @Query("DELETE FROM recipe")
    suspend fun deleteAllRecipe()
}