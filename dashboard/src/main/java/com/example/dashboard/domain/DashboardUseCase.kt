package com.example.dashboard.domain

interface DashboardUseCase {

    suspend fun fetchDashboard(): Any

}