package com.example.detalhes.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remote.SafeResponse
import com.example.remote.safeRequest
import com.example.detalhes.domain.DetalhesUseCase
import com.example.detalhes.view.action.DetalhesAction
import kotlinx.coroutines.launch

class DetalhesViewModel(
    private val detalhesUseCase: DetalhesUseCase
) : ViewModel() {

    var detalhesAction = MutableLiveData<DetalhesAction>()

    fun fetchTeam(name: String, name2: String) {
        viewModelScope.launch {
            when (val response = safeRequest { detalhesUseCase.fetchDetalhes(name) }) {
                is SafeResponse.Success -> {}
                is SafeResponse.GenericError -> DetalhesAction.Error(response.errorBody?.error).run()
                is SafeResponse.NetworkError -> DetalhesAction.Error().run()
            }
        }
    }

    private fun DetalhesAction.run() = detalhesAction.postValue(this)

}