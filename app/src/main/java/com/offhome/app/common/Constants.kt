package com.offhome.app.common

data class Constants(
    val BASE_URL: String = "http://ec2-100-25-149-77.compute-1.amazonaws.com:3000/",

    // Preferences
    val PREF_EMAIL: String = "PREF_EMAIL",
    val PREF_PROVIDER: String = "PREF_PROVIDER",
    val PREF_PROVIDER_PASSWORD: String = "PASSWORD",
    val PREF_PROVIDER_GOOGLE: String = "GOOGLE"
)
