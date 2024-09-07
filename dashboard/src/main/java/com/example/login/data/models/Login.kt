package com.example.login.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("user") var user: String,
    @SerializedName("password") var password: String
) : java.io.Serializable

data class User(
    val id: String = "",
    val nome: String = "",
    val cpf: String = "",
    val email: String = "",
)

data class Reserva(
    val estacionamentoID: String? = null,
    val horaReserva: Timestamp? = null,
    val usuarioID: String? = null
)

data class Estacionamento(
    val id: String = "",
    val nome: String = "",
    val endereco: String = "",
    val location: GeoPoint? = null,
    val telefone: String = "",
    val vagas: Int = 0
)

data class ReservationWithParking(
    val reservation: Reserva,
    val estacionamentoName: String,
    val estacionamentoAddress: String,
    val estacionamentoLocation: GeoPoint?,
    val estacionamentoTelefone: String,
    val estacionamentoVagas: Int
)


