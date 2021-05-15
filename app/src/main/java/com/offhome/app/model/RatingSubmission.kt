package com.offhome.app.model

/**
 * Category class
 * @property usuariParticipant is the participant of the activity
 * @property usuariCreador is the creator of the activity
 * @property dataHoraIni is the date and time of the activity
 * @property valoracio is the rating given to an activity
 * @property comentari is the review made on the activity
 */
data class RatingSubmission(
    val usuariParticipant: String,
    val usuariCreador: String,
    val dataHoraIni: String,
    val valoracio: Int,
    val comentari: String
)
