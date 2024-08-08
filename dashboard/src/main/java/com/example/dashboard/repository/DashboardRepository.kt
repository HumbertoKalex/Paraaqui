package com.example.dashboard.repository

import com.example.dashboard.data.models.PlacesResponse


interface DashboardRepository {

    suspend fun fetchDashboard(
        location: String,
        radius: Int,
        api: String,
        type: String
    ): PlacesResponse
}