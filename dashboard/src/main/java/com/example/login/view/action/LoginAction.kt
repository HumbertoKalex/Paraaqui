package com.example.login.view.action

sealed class LoginAction {
    object LoginSuccess : LoginAction()
    data class Error(val msg: String? = null) : LoginAction()
}
