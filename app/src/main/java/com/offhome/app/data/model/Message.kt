package com.offhome.app.data.model

data class Message(
    var message: String = "",
    var usid_enviador: String = "",
    var timestamp: Long = 0,
    var username_enviador: String = ""
)
