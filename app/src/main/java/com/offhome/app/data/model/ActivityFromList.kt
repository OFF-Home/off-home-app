package com.offhome.app.data.model

/**
 * Data class with the Activity's atributes
 */
data class ActivityFromList(
    val usuariCreador: String,
    val nomCarrer: String,
    val numCarrer: Int,
    val dataHoraIni: String,
    val categoria: String,
    val numParticipants: Int,
    val maxParticipant: Int,
    val titol: String,
    val descripcio: String,
    val dataHoraFi: String,
    val acabada: Int,
    val valoracio: Int,
    var usernameCreador: String = ""
)
