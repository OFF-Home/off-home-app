package com.offhome.app.data.model

/**
 * Data Class *UserInfo*
 *
 * Contains a user's info.
 * @param email a string
 * @param username a string
 * @param uid a string
 * @param birthDate a string
 * @param description a string
 * @param followers follower count. an integer
 * @param following following count. an integer
 * @param darkmode darkmode setting. an integer
 * @param notifications notifications setting. an integer
 * @param estrelles
 * @param language language settings. a string
 */
data class UserInfo(
    val email: String,
    val username: String,
    val uid: String,
    val birthDate: String,
    val description: String,
    var followers: Int,
    val following: Int,
    val darkmode: Int, // a backend no els funcionava el boolean
    val notifications: Int,
    val estrelles: Double,
    val language: String,
    val image: String
)
