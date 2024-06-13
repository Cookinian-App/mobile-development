package com.bangkitcapstone.cookinian.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipe")
class SavedRecipeEntity (
    @field:ColumnInfo(name = "key")
    @PrimaryKey
    val key: String,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "thumb")
    val thumb: String,

    @field:ColumnInfo(name = "times")
    val times: String,

    @field:ColumnInfo(name = "difficulty")
    val difficulty: String,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)