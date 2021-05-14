package com.offhome.app.data.model

/**
 * Data class *SignUpUserData*
 *
 * contains the user information to be sent to the database
 *
 * @author Pau
 * @property email user's email
 * @property username user's username
 * @property password
 * @property birthDate user's birth date
 */
data class SignUpUserData(
    val email: String,
    val usid: String
)
