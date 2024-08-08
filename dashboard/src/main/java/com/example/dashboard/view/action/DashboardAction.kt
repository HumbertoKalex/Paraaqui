package com.example.dashboard.view.action

import com.example.dashboard.data.models.PlacesResponse

sealed class DashboardAction {
    data class DashboardLoaded(val placesResponse: PlacesResponse) : DashboardAction()
    data class Error(val msg: String? = null) : DashboardAction()
}
