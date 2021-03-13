package com.offhome.app.ui.signup

/**
 * Data validation state of the login form.
 */
//plantilla que venia feta
//editant
data class SignUpFormState(
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val birthDateError: Int? = null,
    val isDataValid: Boolean = false
)