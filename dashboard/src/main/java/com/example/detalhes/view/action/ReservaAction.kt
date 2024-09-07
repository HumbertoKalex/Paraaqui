package com.example.detalhes.view.action

import com.example.login.data.models.ReservationWithParking

sealed class ReservaAction {

    data class RecuperarReservas(val reservas: List<ReservationWithParking>) : ReservaAction()
    data class Error(val msg: String? = null) : ReservaAction()

}
