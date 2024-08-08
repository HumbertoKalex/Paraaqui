package com.example.dashboard.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard.R
import com.example.dashboard.domain.DashboardUseCase
import com.example.dashboard.view.action.DashboardAction
import com.example.remote.SafeResponse
import com.example.remote.safeRequest
import com.example.detalhes.view.action.DetalhesAction
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardUseCase: DashboardUseCase
) : ViewModel() {

    var dashboardAction = MutableLiveData<DashboardAction>()

    fun fetchDashboard(currentLocation: LatLng) {
        val location = "${currentLocation.latitude},${currentLocation.longitude}"
        val radius = 5000  // 5km radius
        val type = "parking"
        viewModelScope.launch {
            when (val response = safeRequest {
                dashboardUseCase.fetchDashboard(
                    location = location,
                    radius = radius,
                    api = "AIzaSyApI_q2ZdDTictHlFMZ6HeXA1QfJRE7w8A",
                    type = type
                )
            }) {
                is SafeResponse.Success -> DashboardAction.DashboardLoaded(response.value).run()
                is SafeResponse.GenericError -> DashboardAction.Error(response.errorBody?.error)
                    .run()

                is SafeResponse.NetworkError -> DashboardAction.Error().run()
            }
        }
    }

    private fun DashboardAction.run() = dashboardAction.postValue(this)

}