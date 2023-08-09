package com.example.paraaqui

import com.example.dashboard.di.dashboardModule
import com.example.remote.di.createRemoteModule
import com.example.detalhes.di.detalhesModule
import com.example.login.di.loginModule
import dagger.android.support.DaggerApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class App : DaggerApplication(){

    private val applicationInjector = DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    loginModule,
                    detalhesModule,
                    dashboardModule
                )
            )
        }
        loadKoinModules(
            listOf(
                createRemoteModule(
                    mobileUrl = "https://api.pandascore.co/csgo/")
            )
        )
    }

    override fun applicationInjector() = applicationInjector

}