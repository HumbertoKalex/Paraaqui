package com.example.dashboard.domain

import com.example.dashboard.data.models.PlacesResponse

interface DashboardUseCase {

    suspend fun fetchDashboard(
        location: String,
        radius: Int,
        api: String,
        type: String
    ): PlacesResponse
}