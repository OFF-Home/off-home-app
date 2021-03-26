package com.offhome.app.model

data class ActivityFromList (
        val usuariCreador: String,
        val nomCarrer: String,
        val carrerNum: Int,
        val dataHoraIni: String,
        val categoria: String,
        val maxParticipant: Int,
        val titol: String,
        val descripcio: String,
        val dataHoraFi: String
)