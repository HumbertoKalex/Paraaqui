package com.example.login.domain

interface LoginUseCase {

    suspend fun login(user: String, password: String): Any


}