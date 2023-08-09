package com.example.dashboard.domain

import com.example.dashboard.repository.DashboardRepository

class DashboardUseCaseImpl(
    private val dashboardRepository: DashboardRepository
) : DashboardUseCase {

    override suspend fun fetchDashboard() = Any()

}