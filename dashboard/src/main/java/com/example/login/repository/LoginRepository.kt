package com.example.login.repository

interface LoginRepository {

    suspend fun login(): Any

}