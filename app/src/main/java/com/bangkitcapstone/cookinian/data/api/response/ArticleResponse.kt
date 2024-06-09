package com.bangkitcapstone.cookinian.data.api.response

import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: List<ArticleItem>,

	@field:SerializedName("status")
	val status: Boolean
)
