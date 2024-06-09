package com.bangkitcapstone.cookinian.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article")
data class ArticleItem(
    @field:SerializedName("thumb")
    val thumb: String,

    @field:SerializedName("title")
    val title: String,

    @PrimaryKey
    @field:SerializedName("key")
    val key: String
)