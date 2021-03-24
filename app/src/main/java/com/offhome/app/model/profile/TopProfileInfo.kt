package com.offhome.app.model.profile

// conté la foto de perfil, el nom d'usuari i les estrelles.
// es diu Top Profile Info i va per separat a la resta de info del profile perquè es mostra a la meitat superior del fragment Profile, sense haver d'accedir a AboutMe
data class TopProfileInfo(
    val profilePic: Boolean? = null,
    val username: String,
    val starRating: Int, // encara no hem decidit com funciona (de 0 a 10?)
)
