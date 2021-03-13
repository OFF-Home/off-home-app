package com.offhome.app.ui.signup

/**
 * Data validation state of the login form.
 */
//plantilla que venia feta
data class SignUpFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)