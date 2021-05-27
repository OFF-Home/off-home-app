package com.offhome.app.data.model

/**
 * Data class [ActivityData]
 * It contains the activity information that the database needs to receive
 */
data class ActivityData(
    val nameStreet: String,
    val numberStreet: Int,
    val dateStartTime: String,
    val category: String,
    val maxParticipants: Int,
    val title: String,
    val description: String,
    val dateFinishTime: String,
    val uidCreator: String,
)
