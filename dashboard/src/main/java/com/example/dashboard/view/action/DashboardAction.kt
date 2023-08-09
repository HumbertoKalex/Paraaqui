package com.example.dashboard.view.action

sealed class DashboardAction {
    object DashboardLoaded : DashboardAction()
    data class Error(val msg: String? = null) : DashboardAction()
}
