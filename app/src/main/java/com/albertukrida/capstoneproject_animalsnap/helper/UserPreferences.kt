package com.albertukrida.capstoneproject_animalsnap.helper

import android.content.Context

internal class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val PICTURE = "picture"
        private const val USERID = "userId"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val DATE = "date"
        private const val TOKEN = "token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val SESSION = "session"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(USERID, value.userId)
        editor.putString(PICTURE, value.picture)
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putString(PASSWORD, value.password)
        editor.putString(DATE, value.date)
        editor.putString(TOKEN, value.token)
        editor.putString(REFRESH_TOKEN, value.refresh_token)
        editor.putString(SESSION, value.session)
        editor.apply()
    }
    fun getUser(): UserModel {
        val model = UserModel()
        model.userId = preferences.getString(USERID, "")
        model.picture = preferences.getString(PICTURE, "")
        model.name = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.password = preferences.getString(PASSWORD, "")
        model.date = preferences.getString(DATE, "")
        model.token = preferences.getString(TOKEN, "")
        model.refresh_token = preferences.getString(REFRESH_TOKEN, "")
        model.session = preferences.getString(SESSION, "")
        return model
    }
}