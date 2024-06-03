package com.bangkitcapstone.cookinian.data.api.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: List<CategoryItem>,

	@field:SerializedName("status")
	val status: Boolean
)

data class CategoryItem(

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("key")
	val key: String
)
