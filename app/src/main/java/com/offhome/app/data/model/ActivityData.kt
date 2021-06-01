package com.offhome.app.data.model

/**
 * Data class [ActivityData]
 * It contains the activity information that the database needs to receive
 */
data class ActivityData(
    val nomCarrer: String,
    val carrerNum: Int,
    val latitud: Double,
    val longitud: Double,
    val dataHoraIni: String,
    val categoria: String,
    val maxParticipants: Int,
    val titol: String,
    val descripcio: String,
    val dataHoraFi: String,
    val uid_creador: String,
)
