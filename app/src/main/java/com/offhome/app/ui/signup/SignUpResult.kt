package com.offhome.app.ui.signup

/**
 * Data Class *SignUpResult*
 *
 * expresses the result of a sign-up execution.
 * @author Ferran
 * @property success: means that the sign-up was successful
 * @property error: means that there was an error signing up. contains the ID to a string explaining the error.
 */
data class SignUpResult(
    val success: Boolean? = null, // bool innecessari però esque anira checkejant des de la Activity si és null.
    val error: Int? = null
)
