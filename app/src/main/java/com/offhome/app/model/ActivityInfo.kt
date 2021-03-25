package com.offhome.app.model

data class ActivityInfo (
    val title: String,
    val description: String,
    val max_participants: Int,
    val category: Category,
    val nomCarrer: String,
    val numCarrer: Int,
    val dataHoraIni: String,
    val dataHoraFi: String,
    val usuariCreador: String,
)