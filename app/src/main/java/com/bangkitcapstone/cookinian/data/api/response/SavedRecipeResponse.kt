package com.bangkitcapstone.cookinian.data.api.response

import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.google.gson.annotations.SerializedName

data class SavedRecipeResponse(

	@field:SerializedName("recipes")
	val recipes: List<RecipeItem>,

	@field:SerializedName("error")
	val error: Boolean
)
