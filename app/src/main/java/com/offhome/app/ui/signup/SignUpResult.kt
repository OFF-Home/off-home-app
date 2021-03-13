package com.offhome.app.ui.signup

/**
 * Authentication result : success (user details) or error message.
 */
// plantilla que venia feta
data class SignUpResult(
    val success: SignedUpUserView? = null,
    val error: Int? = null
)
