package com.example.flourishtavelapp.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.flourishtavelapp.data.model.UserInfo

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "flourish_travel_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_AVATAR = "user_avatar"
    }

    fun saveSession(accessToken: String, refreshToken: String, user: UserInfo) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_NAME, user.fullName)
            putString(KEY_USER_ROLE, user.role)
            putString(KEY_USER_AVATAR, user.avatarUrl)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun getUserInfo(): UserInfo? {
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        val email = prefs.getString(KEY_USER_EMAIL, "") ?: ""
        val name = prefs.getString(KEY_USER_NAME, "") ?: ""
        val role = prefs.getString(KEY_USER_ROLE, "") ?: ""
        val avatarUrl = prefs.getString(KEY_USER_AVATAR, null)
        return UserInfo(id, email, name, role, avatarUrl)
    }

    fun isLoggedIn(): Boolean = getAccessToken() != null

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
