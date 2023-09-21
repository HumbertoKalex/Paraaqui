package com.example.detalhes.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dashboard.R
import com.example.dashboard.databinding.FragmentCheckBinding
import com.example.detalhes.view.action.DetalhesAction
import com.example.utils.core.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


private var countDownTimer: CountDownTimer? = null

class CheckFragment : BaseFragment() {

    private lateinit var binding: FragmentCheckBinding
    private val viewModel: DetalhesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = getDataBinding(inflater, container, R.layout.fragment_check)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    private fun observeActions() {
        viewModel.detalhesAction.observe(viewLifecycleOwner) {
            when (it) {
                is DetalhesAction.DetalhesLoaded -> {}

                is DetalhesAction.Error -> showError(it.msg ?: "Generic Error")
            }
        }

        binding.iconEnd.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_dashboard_fragment)
        }
        binding.iconStart.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_detalhes_fragment)
        }

        setupTimer()
    }

    private fun setupTimer() {
        val totalTime = 3600000L

        val countDownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((millisUntilFinished.toFloat() / totalTime) * 100).toInt()
                val secondsRemaining = millisUntilFinished / 1000
                val hours = secondsRemaining / 3600
                val minutes = (secondsRemaining % 3600) / 60
                val seconds = secondsRemaining % 60
                binding.progressBar.progress = progress
                binding.timerText.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                binding.progressBar.progress = 0
                binding.timerText.text = "00:00:00"
            }
        }

        countDownTimer.start()


    }

    private fun showError(error: String) = Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}