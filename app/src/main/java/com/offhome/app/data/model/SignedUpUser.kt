package com.offhome.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class SignedUpUser(
    val email: String,
    val username: String,
    val password: String,
    val birthDate: String
)
