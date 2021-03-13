package com.offhome.app.ui.signup

/**
 * Authentication result : success (user details) or error message.
 *
 * aixo es el resultat del SIGN UP. per tant, els possibles errors son:
 *      email ja existeix
 *      username ja existeix
 *      error al iniciar amb Google, suposo
 */
//plantilla que venia feta
data class SignUpResult(
    val success: SignedUpUserView? = null,
    val error: Int? = null
)