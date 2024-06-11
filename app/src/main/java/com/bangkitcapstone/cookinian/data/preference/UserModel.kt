package com.bangkitcapstone.cookinian.data.preference

data class UserModel(
    val name: String,
    val avatarUrl: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)