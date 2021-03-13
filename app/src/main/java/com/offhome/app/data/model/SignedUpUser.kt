package com.offhome.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
//plantilla que venia feta
data class SignedUpUser(
    val userId: String,
    val displayName: String
)