package com.dicoding.intermediate1

import com.google.gson.annotations.SerializedName

data class AllStoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<UserDataResponse>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

