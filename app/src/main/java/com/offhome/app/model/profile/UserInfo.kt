package com.offhome.app.model.profile

data class UserInfo(
    val email: String,
    val username: String,
    val password: String,
    val birthDate: String,
    val description: String,
    val followers: Int,
    val following: Int,
    val darkmode: Int, // a backend no els funcionava el boolean
    val notifications: Int,
    val estrelles: Int,
    val tags: String,
    val language: String
)
