package com.example.dashboard.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dashboard.view.action.DashboardAction
import com.example.firebase.FirebaseHandler
import com.google.firebase.Timestamp
import java.util.Date

class DashboardViewModel(
    private val firebaseHandler: FirebaseHandler
) : ViewModel() {

    var dashboardAction = MutableLiveData<DashboardAction>()

    fun fetchEstacionamentos() {
        firebaseHandler.getEstacionamentos { estacionamentos, error ->
            if (estacionamentos != null) {
                DashboardAction.DashboardLoaded(estacionamentos).run()
            } else {
                DashboardAction.Error(error?.message ?: "Erro ao buscar estacionamentos").run()
            }
        }
    }

    fun saveReserva(estacionamentoID: String) {
        val horaAtual = Timestamp.now()

        firebaseHandler.makeReservation(estacionamentoID, horaAtual) { result ->
            if (result.isSuccessful) {
                DashboardAction.ReservaSuccess.run()
            } else {
                DashboardAction.Error(result.exception?.message ?: "Erro ao salvar reserva").run()
            }
        }
    }

    private fun DashboardAction.run() = dashboardAction.postValue(this)

}