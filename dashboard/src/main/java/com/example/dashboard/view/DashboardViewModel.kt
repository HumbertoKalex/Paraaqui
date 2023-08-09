package com.example.dashboard.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard.domain.DashboardUseCase
import com.example.dashboard.view.action.DashboardAction
import com.example.remote.SafeResponse
import com.example.remote.safeRequest
import com.example.detalhes.view.action.DetalhesAction
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardUseCase: DashboardUseCase
) : ViewModel() {

    var dashboardAction = MutableLiveData<DashboardAction>()

    fun fetchDashboard() {
        viewModelScope.launch {
            when (val response = safeRequest { dashboardUseCase.fetchDashboard() }) {
                is SafeResponse.Success -> {}
                is SafeResponse.GenericError -> DashboardAction.Error(response.errorBody?.error).run()
                is SafeResponse.NetworkError -> DashboardAction.Error().run()
            }
        }
    }

    private fun DashboardAction.run() = dashboardAction.postValue(this)

}