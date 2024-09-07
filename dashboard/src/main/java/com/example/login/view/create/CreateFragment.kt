package com.example.login.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentCreateBinding
import com.example.login.view.action.LoginAction
import com.example.dashboard.databinding.FragmentLoginBinding
import com.example.login.view.action.CreateAction
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateBinding

    private val viewModel: CreateViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_create)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    private fun observeActions() {
        viewModel.createAction.observe(viewLifecycleOwner) {
            when (it) {
                is CreateAction.CreateSuccess -> findNavController().navigate(R.id.action_createFragment_to_plano_fragment)

                is CreateAction.Error -> showError(it.msg ?: "Generic Error")
            }
        }

        binding.btnCreateContinue.setOnClickListener {
            viewModel.create(binding.edtEmail.text.toString(), binding.edtSenha.text.toString(), binding.edtNome.text.toString(), binding.edtCpf.text.toString())
        }
    }

    private fun showError(error: String) =
        Toast.makeText(context, error, Toast.LENGTH_SHORT)
            .show()
}