package com.memori.app.data.remote

import android.content.Context

object SessionManager {
    private const val PREF_NAME = "memori_prefs"

    fun salvarSessao(context: Context, userId: String, token: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString("user_id", userId)
            .putString("token", token)
            .apply()
        RetrofitClient.token = token
    }

    fun getUserId(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString("user_id", "") ?: ""

    fun carregarToken(context: Context) {
        RetrofitClient.token =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("token", "") ?: ""
    }
}