package com.offhome.app.ui.signup

/**
 * Data validation state of the login form.
 * porta el track de si tot és vàlid, a nivell dels strings sense haver passat res a capes de dades
 */
//plantilla que venia feta
//editant
data class SignUpFormState(
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    //val birthDateError: Int? = null,  //impossible, restringirem els possibles valors d'entrada amb la UI
    val isDataValid: Boolean = false
)