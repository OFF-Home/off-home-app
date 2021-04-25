package com.offhome.app.common

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager {

    companion object Factory {
        private val appSettingsFile ="APP_SETTINGS"
        private fun getSharedPreferences(): SharedPreferences {
            return MyApp.getInstance().getSharedPreferences(
                appSettingsFile,
                Context.MODE_PRIVATE
            )
        }
        fun setStringValue(dataLabel: String, dataValue: String) {
            val editor = getSharedPreferences().edit()
            editor.putString(dataLabel, dataValue)
            editor.apply()
        }
        fun setStringValue(dataLabel: String, dataValue: Boolean) {
            val editor = getSharedPreferences().edit()
            editor.putBoolean(dataLabel, dataValue)
            editor.apply()
        }
        fun getStringValue(dataLabel: String): String? {
            return getSharedPreferences().getString(dataLabel, null)
        }
        fun getBooleanValue(dataLabel: String): Boolean {
            return getSharedPreferences().getBoolean(dataLabel, false)
        }
    }
}
