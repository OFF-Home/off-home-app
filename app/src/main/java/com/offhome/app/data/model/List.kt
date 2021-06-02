package com.offhome.app.data.model

import kotlin.collections.List

data class List (
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)
