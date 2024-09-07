package com.example.detalhes.plano.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentPlanoBinding
import com.example.detalhes.plano.view.action.PlanoAction
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanoFragment : BaseFragment() {

    private lateinit var binding: FragmentPlanoBinding
    private val viewModel: PlanoViewModel by viewModel()
    private lateinit var planoAdapter: PlanoAdapter
    private val plans = listOf(
        Plan(
            name = "Plano Ouro",
            price = "R$85,99 / mês",
            description = "Para estacionar regularmente",
            features = listOf("-> 30 vagas mensais", "-> Escolha livre entre os estacionamentos"),
            id = "plano_ouro" // Add unique ID for the plan
        ),
        Plan(
            name = "Plano Prata",
            price = "R$24,99 / mês",
            description = "Para estacionar ocasionalmente",
            features = listOf("-> 15 vagas mensais", "-> Escolha livre entre os estacionamentos"),
            id = "plano_prata" // Add unique ID for the plan
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_plano)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeActions()
        binding.toggleGroup.check(R.id.btn_anual)
    }

    private fun setupAdapter() {
        planoAdapter = PlanoAdapter(plans) { selectedPlan ->
            viewModel.updateUserPlan(selectedPlan.id)
        }
        binding.viewPagerPlano.adapter = planoAdapter
    }

    private fun observeActions() {
        viewModel.detalhesAction.observe(viewLifecycleOwner) {
            when (it) {
                is PlanoAction.UserFetched -> {
                    // Handle user details
                }

                is PlanoAction.PlanoUpdated -> {
                    Toast.makeText(context, "Plano atualizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_planoFragment_to_detalhes_fragment)
                }

                is PlanoAction.Error -> showError(it.msg ?: "Generic Error")
            }
        }
    }

    private fun showError(error: String) =
        Toast.makeText(context, error, Toast.LENGTH_SHORT)
            .show()
}
