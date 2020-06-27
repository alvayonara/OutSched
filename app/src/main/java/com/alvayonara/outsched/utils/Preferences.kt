package com.alvayonara.outsched.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    companion object {
        const val OUT_SCHEDULE_PREF = "USER_PREF"
        const val ON_BOARDING = "on_boarding"
    }

    private val sharedPref = context.getSharedPreferences(OUT_SCHEDULE_PREF, 0)

    fun setValues(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getValues(key: String): String? = sharedPref.getString(key, "")
}