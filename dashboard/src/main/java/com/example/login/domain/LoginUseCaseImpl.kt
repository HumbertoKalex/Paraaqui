package com.example.login.domain

import com.example.login.repository.LoginRepository

class LoginUseCaseImpl(
    private val loginRepository: LoginRepository
) : LoginUseCase {

    override suspend fun login(user: String, password: String) = Any()
}