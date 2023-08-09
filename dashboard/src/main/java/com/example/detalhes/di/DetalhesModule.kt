package com.example.detalhes.di

import com.example.detalhes.domain.DetalhesUseCase
import com.example.detalhes.domain.DetalhesUseCaseImpl
import com.example.detalhes.repository.DetalhesApi
import com.example.detalhes.repository.DetalhesRepository
import com.example.detalhes.repository.DetalhesRepositoryImpl
import com.example.detalhes.view.DetalhesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val detalhesModule = module {
    factory { get<Retrofit>(named("mobileRetrofit")).create(DetalhesApi::class.java) }
    factory<DetalhesRepository> { DetalhesRepositoryImpl(get()) }
    factory<DetalhesUseCase> { DetalhesUseCaseImpl(get()) }

    viewModel { DetalhesViewModel(get()) }
}