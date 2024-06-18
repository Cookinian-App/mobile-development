package com.bangkitcapstone.cookinian.data.api.response

import com.bangkitcapstone.cookinian.data.local.entity.RecipesRecommendationItem
import com.google.gson.annotations.SerializedName

data class RecipesRecommendationResponse(

	@field:SerializedName("recipes")
	val recipes: List<RecipesRecommendationItem>,

	@field:SerializedName("error")
	val error: Boolean
)


