package com.bangkitcapstone.cookinian.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bangkitcapstone.cookinian.data.local.entity.SavedRecipeEntity

@Dao
interface SavedRecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFromApi(recipe: List<SavedRecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToLocal(recipe: SavedRecipeEntity)

    @Query("DELETE FROM saved_recipe")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteFromLocal(recipe: SavedRecipeEntity)

    @Query("SELECT * FROM saved_recipe")
    fun getSavedRecipe(): LiveData<List<SavedRecipeEntity>>

    @Query("SELECT * FROM saved_recipe where bookmarked = 1")
    fun getBookmarkedNews(): LiveData<List<SavedRecipeEntity>>

    @Query("SELECT EXISTS(SELECT * FROM saved_recipe WHERE `key` = :key)")
    fun isSavedRecipe(key: String): LiveData<Boolean>
}