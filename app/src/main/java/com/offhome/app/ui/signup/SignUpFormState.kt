package com.offhome.app.ui.signup

/**
 * Data Class *SignUpFormState*
 *
 * Data validation state of the signUp form.
 * Keeps track of whether the input strings are valid (have the appropriate format)
 * Contains strings indicating the errors
 *
 * @author Ferran
 * @property emailError: string indicating a format error on the input email
 * @property usernameError: string indicating a format error on the input username
 * @property passwordError: string indicating a format error on the input password
 * @property birthDateError: string indicating a format error on the input birth date
 * @property isDataValid: boolean indicating whether the data is valid
 */
data class SignUpFormState(
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val birthDateError: Int? = null, // l'unic error possible hauria de ser que estigui buit.
    val isDataValid: Boolean = false
)
