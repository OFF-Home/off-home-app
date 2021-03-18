package com.offhome.app.ui.signup

/**
 * Authentication result : success (user details) or error message.
 *
 * aixo es el resultat del SIGN UP. per tant, els possibles errors son:
 *      email ja existeix
 *      username ja existeix
 *      error al iniciar amb Google, suposo
 */
// plantilla que venia feta
data class SignUpResult(
    val success: Boolean? = null,       //bool innecessari però esque anira checkejant des de la Activity si és null.
    val error: Int? = null
)

/*class SignUpResult {
    var isSuccessful : Boolean? = null
    var errorMessage: Int? = null
}*/