package com.bangkitcapstone.cookinian.data.api.response

import com.google.gson.annotations.SerializedName

data class ArticleDetailResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("results")
	val results: ArticleDetailResults,

	@field:SerializedName("status")
	val status: Boolean
)

data class ArticleDetailResults(

	@field:SerializedName("thumb")
	val thumb: String,

	@field:SerializedName("date_published")
	val datePublished: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("descriptionRaw")
	val descriptionRaw: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("tags")
	val tags: List<TagsItem>
)

data class TagsItem(

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("key")
	val key: String
)
