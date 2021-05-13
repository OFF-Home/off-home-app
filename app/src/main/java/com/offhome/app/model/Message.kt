package com.offhome.app.model



import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var message: String = "",
    var usid_enviador: String = "",
    var timestamp: Long = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "message" to message,
            "usid_enviador" to usid_enviador
        )
    }

}
