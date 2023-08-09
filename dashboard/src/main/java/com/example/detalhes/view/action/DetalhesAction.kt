package com.example.detalhes.view.action

sealed class DetalhesAction {
    object DetalhesLoaded : DetalhesAction()

    data class Error(val msg: String? = null) : DetalhesAction()
}
