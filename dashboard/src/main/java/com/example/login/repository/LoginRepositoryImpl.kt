package com.example.login.repository

import android.content.Context
import com.example.login.data.models.Login
import com.google.gson.Gson

class LoginRepositoryImpl(
    private val loginApi: LoginApi
) : LoginRepository {

    override suspend fun login() = Any()

}

fun Context.readUserFromAssets(): Login? {
    val assetManager = this.assets
    return try {
        val inputStream = assetManager.open("login.json") // Replace 'filename.json' with your actual filename.
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Gson().fromJson(jsonString, Login::class.java)
    } catch (exception: Exception) {
        exception.printStackTrace()
        null
    }
}

