package com.example.dashboard.di

import com.example.dashboard.domain.DashboardUseCase
import com.example.dashboard.domain.DashboardUseCaseImpl
import com.example.detalhes.repository.DetalhesApi
import com.example.dashboard.repository.DashboardRepository
import com.example.dashboard.repository.DashboardRepositoryImpl
import com.example.dashboard.view.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {
    factory { get<Retrofit>(named("mobileRetrofit")).create(DetalhesApi::class.java) }
    factory<DashboardRepository> { DashboardRepositoryImpl(get()) }
    factory<DashboardUseCase> { DashboardUseCaseImpl(get()) }

    viewModel { DashboardViewModel(get()) }
}