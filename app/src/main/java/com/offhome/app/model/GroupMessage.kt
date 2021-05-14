package com.offhome.app.model



import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupMessage(
    var message: String = "",
    var userCreator: String = "",
    var userNameSender: String = "",
    var userSender: String = "",
    var dataHoraIni: String = "",
    var timestamp: Long = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "message" to message,
            "userCreator" to userCreator,
            "userSender" to userSender,
            "dataHI" to dataHoraIni,
        )
    }
}
