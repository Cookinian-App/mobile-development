package com.bangkitcapstone.cookinian.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe")
data class RecipeItem(

    @field:SerializedName("difficulty")
    val difficulty: String,

    @field:SerializedName("times")
    val times: String,

    @field:SerializedName("thumb")
    val thumb: String,

    @field:SerializedName("calories")
    val calories: String,

    @field:SerializedName("title")
    val title: String,

    @PrimaryKey
    @field:SerializedName("key")
    val key: String,

    @field:SerializedName("serving")
    val serving: String
)