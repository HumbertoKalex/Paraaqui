package com.example.detalhes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.login.data.models.Login
import com.example.dashboard.databinding.FragmentDashboardBinding
import com.example.dashboard.databinding.FragmentDetalhesBinding
import com.example.detalhes.view.action.DetalhesAction
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalhesFragment : BaseFragment() {

    private lateinit var binding: FragmentDetalhesBinding
    private val viewModel: DetalhesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_detalhes)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    private fun observeActions() {
        viewModel.detalhesAction.observe(viewLifecycleOwner) {
            when (it) {
                is DetalhesAction.DetalhesLoaded ->{}

                is DetalhesAction.Error -> showError(it.msg ?: "Generic Error")
            }
        }

        binding.linearDetalhes.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showError(error: String) =
        Toast.makeText(context, error, Toast.LENGTH_SHORT)
            .show()
}