package com.bangkitcapstone.cookinian.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getArticles(): PagingSource<Int, ArticleItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleItem>)

    @Query("DELETE FROM article")
    suspend fun deleteAllArticle()
}