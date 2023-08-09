package com.example.login.di

import com.example.login.domain.LoginUseCase
import com.example.login.domain.LoginUseCaseImpl
import com.example.login.repository.LoginApi
import com.example.login.repository.LoginRepository
import com.example.login.repository.LoginRepositoryImpl
import com.example.login.view.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val loginModule = module {
    factory { get<Retrofit>(named("mobileRetrofit")).create(LoginApi::class.java) }
    factory<LoginRepository> { LoginRepositoryImpl(get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get()) }

    viewModel { LoginViewModel(get()) }
}