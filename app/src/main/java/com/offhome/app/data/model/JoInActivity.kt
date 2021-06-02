package com.offhome.app.data.model

/**
 * Data class model for Joining an activity
 */
data class JoInActivity(
    val usuariCreador: String,
    val dataHoraIni: String,
    val usuariParticipant: String,
    val uid_creador: String,
    val uid_participant: String
)
