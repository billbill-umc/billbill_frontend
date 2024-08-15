package com.example.billbill_template

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApp : Application() {

    companion object {
        private const val PREFS_NAME = "MyApp"
        private lateinit var preferences: SharedPreferences

        fun init(context: Context) {
            preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        fun getString(key: String, defaultValue: String? = null): String? {
            return preferences.getString(key, defaultValue)
        }

        fun putString(key: String, value: String) {
            preferences.edit().putString(key, value).apply()
        }

        fun remove(key: String) {
            preferences.edit().remove(key).apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}
