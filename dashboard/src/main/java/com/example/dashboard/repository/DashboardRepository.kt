package com.example.dashboard.repository


interface DashboardRepository {

    suspend fun fetchDashboard(name: String): Any

}