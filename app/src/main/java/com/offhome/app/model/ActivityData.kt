package com.offhome.app.model

/**
 * Data class [ActivityData]
 * It contains the activity information that the database needs to receive
 */
data class ActivityData(
    val nomCarrer: String,
    val carrerNum: Int,
    val dataHoraIni: String,
    val categoria: String,
    val maxParticipant: Int,
    val titol: String,
    val descripcio: String,
    val dataHoraFi: String,
)
