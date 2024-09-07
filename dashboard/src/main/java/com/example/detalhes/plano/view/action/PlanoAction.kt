package com.example.detalhes.plano.view.action

import com.example.login.data.models.User

sealed class PlanoAction {

    data class Error(val msg: String? = null) : PlanoAction()
    data class UserFetched(val user: User) : PlanoAction()
    object PlanoUpdated : PlanoAction()

}
