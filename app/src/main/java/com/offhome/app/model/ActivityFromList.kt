package com.offhome.app.model

/**
 * Data class with the Activity's atributes
 */
data class ActivityFromList (
    val nomCarrer: String,
    val carrerNum: Int,
    val dataHoraIni: String,
    val categoria: String,
    val maxParticipant: String,
    val titol: String,
    val descripcio: String,
    val dataHoraFi: String
)