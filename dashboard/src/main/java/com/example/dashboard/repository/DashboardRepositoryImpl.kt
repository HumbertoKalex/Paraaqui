package com.example.dashboard.repository

import com.example.detalhes.repository.DetalhesApi

class DashboardRepositoryImpl(
    private val temDetailService: DetalhesApi
) : DashboardRepository {

    override suspend fun fetchDashboard(name: String) = Any()

}