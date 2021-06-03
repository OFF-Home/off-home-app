package com.offhome.app.data.model

data class ActivityDataForInvite(
    val maxParticipant: Int,
    val nRemainingParticipants: Int,
    val usuariCreador: String,
    val dataHoraIni: String,

    val categoria: String,
    val titol: String,
    val descripcio: String,
)
