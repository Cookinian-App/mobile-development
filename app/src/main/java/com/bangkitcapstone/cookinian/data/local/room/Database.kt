package com.bangkitcapstone.cookinian.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.data.local.entity.RemoteKeys

@Database(entities = [RecipeItem::class, ArticleItem::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class CookinianDatabase : RoomDatabase(){

    abstract fun recipeDao(): RecipeDao
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: CookinianDatabase? = null

        fun getInstance(context: Context): CookinianDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CookinianDatabase::class.java, "Cookinian.db"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}