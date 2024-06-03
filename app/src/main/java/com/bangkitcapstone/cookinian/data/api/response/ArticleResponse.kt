package com.bangkitcapstone.cookinian.data.api.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: List<ArticleItem>,

	@field:SerializedName("status")
	val status: Boolean
)

data class ArticleItem(

	@field:SerializedName("thumb")
	val thumb: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("key")
	val key: String
)
