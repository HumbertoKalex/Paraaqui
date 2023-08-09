package com.example.login.repository

import retrofit2.http.GET

interface LoginApi {
    @GET("/matches")
    suspend fun login(): Any
}