package com.example.dashboard.domain

import com.example.dashboard.data.models.PlacesResponse
import com.example.dashboard.repository.DashboardRepository

class DashboardUseCaseImpl(
    private val dashboardRepository: DashboardRepository
) : DashboardUseCase {

    override suspend fun fetchDashboard(
        location: String,
        radius: Int,
        api: String,
        type: String
    ): PlacesResponse = dashboardRepository.fetchDashboard(
        location = location,
        radius = radius,
        api = api,
        type = type
    )
}