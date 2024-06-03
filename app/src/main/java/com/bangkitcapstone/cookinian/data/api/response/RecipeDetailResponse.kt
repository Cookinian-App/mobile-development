package com.bangkitcapstone.cookinian.data.api.response

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: RecipeDetailResults,

	@field:SerializedName("status")
	val status: Boolean
)

data class RecipeDetailResults(

	@field:SerializedName("difficulty")
	val difficulty: String,

	@field:SerializedName("times")
	val times: String,

	@field:SerializedName("ingredient")
	val ingredient: List<String>,

	@field:SerializedName("thumb")
	val thumb: Any,

	@field:SerializedName("author")
	val author: Author,

	@field:SerializedName("step")
	val step: List<String>,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("needItem")
	val needItem: List<Any>,

	@field:SerializedName("desc")
	val desc: String
)

data class Author(

	@field:SerializedName("datePublished")
	val datePublished: String,

	@field:SerializedName("user")
	val user: String
)
