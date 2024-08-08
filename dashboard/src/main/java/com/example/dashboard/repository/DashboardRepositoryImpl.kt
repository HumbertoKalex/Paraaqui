package com.example.dashboard.repository

import com.example.detalhes.repository.DetalhesApi

class DashboardRepositoryImpl(
    private val temDetailService: DetalhesApi
) : DashboardRepository {

    override suspend fun fetchDashboard(location: String, radius: Int, api: String, type: String) =
        temDetailService.searchNearby(
            location = location,
            radius = radius,
            apiKey = api,
            type = type
        )

}