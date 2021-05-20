package com.offhome.app.common

data class Constants(
    val BASE_URL: String = "https://off-home.herokuapp.com/",

    // Preferences
    val PREF_EMAIL: String = "PREF_EMAIL",
    val PREF_USERNAME: String = "PREF_NAME",
    val PREF_PROVIDER: String = "PREF_PROVIDER",
    val PREF_PROVIDER_PASSWORD: String = "PASSWORD",
    val PREF_PROVIDER_GOOGLE: String = "GOOGLE",
    val PREF_UID: String = "PREF_UID",
    val PREF_PHOTO: String = "URL_PHOTO",
    val PICK_PHOTO_FOR_AVATAR: Int = 1,
    val SELECT_PHOTO_GALLERY: Int = 1,
    val REQUEST_IMAGE_CAPTURE: Int = 1
)
