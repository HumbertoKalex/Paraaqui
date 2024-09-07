package com.example.dashboard.view.action

import com.example.login.data.models.Estacionamento

sealed class DashboardAction {
    data class DashboardLoaded(val estacionamentos: List<Estacionamento>) : DashboardAction()
    object ReservaSuccess : DashboardAction()
    data class Error(val msg: String? = null) : DashboardAction()
}
