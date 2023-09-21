package com.example.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.login.view.action.LoginAction
import com.example.dashboard.databinding.FragmentLoginBinding
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_login)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    private fun observeActions() {
        viewModel.loginAction.observe(viewLifecycleOwner) {
            when (it) {
                is LoginAction.LoginSuccess -> findNavController().navigate(R.id.action_loginFragment_to_detalhes_fragment)

                is LoginAction.Error -> showError(it.msg ?: "Generic Error")
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.edtUser.text.toString(), binding.edtSenha.text.toString(), context)
        }
    }

    private fun showError(error: String) =
        Toast.makeText(context, error, Toast.LENGTH_SHORT)
            .show()
}