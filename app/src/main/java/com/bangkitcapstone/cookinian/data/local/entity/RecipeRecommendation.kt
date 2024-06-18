package com.bangkitcapstone.cookinian.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe_recommendation")
data class RecipesRecommendationItem(

    @field:SerializedName("difficulty")
    val difficulty: String,

    @field:SerializedName("times")
    val times: String,

    @field:SerializedName("thumb")
    val thumb: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("matches")
    val matches: Int,

    @PrimaryKey
    @field:SerializedName("key")
    val key: String
)