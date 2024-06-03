package com.bangkitcapstone.cookinian.data.api.response

import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.google.gson.annotations.SerializedName

data class RecipeResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: List<RecipeItem>,

	@field:SerializedName("status")
	val status: Boolean
)

