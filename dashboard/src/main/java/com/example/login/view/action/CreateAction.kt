package com.example.login.view.action

sealed class CreateAction {
    object CreateSuccess : CreateAction()
    data class Error(val msg: String? = null) : CreateAction()
}
