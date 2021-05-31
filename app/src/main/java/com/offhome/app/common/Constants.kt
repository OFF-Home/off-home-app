package com.offhome.app.common

data class Constants(
    val BASE_URL: String = "http://ec2-34-229-177-211.compute-1.amazonaws.com:3000/",
    // val BASE_URL: String = "http://10.0.2.2:3000/",

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
    val REQUEST_IMAGE_CAPTURE: Int = 1,
    val DARK_MODE: Boolean = false,

    //per onboarding
    val PREF_IS_NOT_FIRST_TIME_OPENING_APP: String = "PREF_IS_FIRST_TIME_OPENING_APP"
)
