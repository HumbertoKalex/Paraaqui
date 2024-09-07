package com.example.detalhes.view.action

import com.example.login.data.models.User

sealed class DetalhesAction {

    data class UserFetched(val user: User) : DetalhesAction()
    data class Error(val msg: String? = null) : DetalhesAction()

}
