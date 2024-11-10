package com.example.narrate

import android.content.Context

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(username: String, date: String, imageResId: Int) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("date", date)
            putInt("imageResId", imageResId)
            apply()
        }
    }

    fun getUserSession(): UserSession? {
        val username = sharedPreferences.getString("username", null)
        val date = sharedPreferences.getString("date", null)
        val imageResId = sharedPreferences.getInt("imageResId", -1)

        return if (username != null && date != null && imageResId != -1) {
            UserSession(username, date, imageResId)
        } else {
            null
        }
    }

    fun clearSession() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}

data class UserSession(val username: String, val date: String, val imageResId: Int)
