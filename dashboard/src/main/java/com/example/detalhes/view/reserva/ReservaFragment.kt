package com.example.detalhes.view.reserva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dashboard.databinding.FragmentReservaBinding
import com.example.detalhes.view.action.ReservaAction
import com.example.login.data.models.ReservationWithParking
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReservaFragment : BaseFragment() {

    private lateinit var binding: FragmentReservaBinding
    private lateinit var reservationAdapter: ReservaAdapter
    private val viewModel: ReservaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeActions()
        viewModel.fetchReservas()
    }

    private fun setupRecyclerView() {
        reservationAdapter = ReservaAdapter(emptyList()) { reservation ->
            showDetails(reservation)
        }
        binding.recyclerViewReservations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reservationAdapter
        }
    }

    private fun observeActions() {
        viewModel.reservaAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is ReservaAction.RecuperarReservas -> {
                    reservationAdapter.updateReservations(action.reservas)
                }

                is ReservaAction.Error -> {
                    Toast.makeText(
                        context,
                        action.msg ?: "An error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showDetails(reservation: ReservationWithParking) {
    }
}