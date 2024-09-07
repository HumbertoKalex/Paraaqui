package com.example.detalhes.view.reserva

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detalhes.view.action.ReservaAction
import com.example.firebase.FirebaseHandler
import com.example.login.data.models.ReservationWithParking
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val firebaseHandler: FirebaseHandler,
) : ViewModel() {

    var reservaAction = MutableLiveData<ReservaAction>()

    fun fetchReservas() {
        viewModelScope.launch {
            val currentUser = firebaseHandler.getCurrentUser()
            if (currentUser != null) {
                firebaseHandler.getUserReservations(currentUser.uid) { reservations, error ->
                    if (reservations != null) {
                        val reservationsWithParking = mutableListOf<ReservationWithParking>()
                        reservations.forEach { reservation ->
                            firebaseHandler.getEstacionamento(
                                reservation.estacionamentoID ?: "0"
                            ) { estacionamento, estError ->
                                if (estacionamento != null) {
                                    val reservationWithParking = ReservationWithParking(
                                        reservation = reservation,
                                        estacionamentoName = estacionamento.nome,
                                        estacionamentoAddress = estacionamento.endereco,
                                        estacionamentoLocation = estacionamento.location,
                                        estacionamentoTelefone = estacionamento.telefone,
                                        estacionamentoVagas = estacionamento.vagas
                                    )
                                    reservationsWithParking.add(reservationWithParking)
                                    if (reservationsWithParking.size == reservations.size) {
                                        ReservaAction.RecuperarReservas(reservationsWithParking)
                                            .run()
                                    }
                                } else {
                                    ReservaAction.Error(
                                        estError?.message ?: "Erro ao buscar estacionamento"
                                    ).run()
                                }
                            }
                        }
                    } else {
                        ReservaAction.Error(error?.message ?: "Failed to fetch reservations").run()
                    }
                }
            } else {
                ReservaAction.Error("User not logged in").run()
            }
        }
    }

    private fun ReservaAction.run() = reservaAction.postValue(this)

}