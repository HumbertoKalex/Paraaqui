package com.example.detalhes.view.reserva

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.databinding.ItemReservationBinding
import com.example.login.data.models.ReservationWithParking


class ReservaAdapter(
    private var reservations: List<ReservationWithParking>,
    private val onDetailsClick: (ReservationWithParking) -> Unit
) :
    RecyclerView.Adapter<ReservaAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(private val binding: ItemReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reservation: ReservationWithParking) {
            binding.parkingName.text = reservation.estacionamentoName
            binding.parkingAddress.text = reservation.estacionamentoAddress
            binding.reservationDate.text = reservation.reservation.horaReserva?.toDate().toString()

//            binding.viewDetails.setOnClickListener {
//                onDetailsClick(reservationWithParking)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding =
            ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    override fun getItemCount(): Int = reservations.size

    fun updateReservations(newReservations: List<ReservationWithParking>) {
        reservations = newReservations
        notifyDataSetChanged()
    }
}
