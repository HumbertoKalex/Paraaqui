package com.example.detalhes.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detalhes.domain.DetalhesUseCase
import com.example.detalhes.view.action.DetalhesAction
import com.example.firebase.FirebaseHandler
import com.example.remote.SafeResponse
import com.example.remote.safeRequest
import kotlinx.coroutines.launch

class DetalhesViewModel(
    private val detalhesUseCase: DetalhesUseCase,
    private val firebaseHandler: FirebaseHandler,
) : ViewModel() {

    var detalhesAction = MutableLiveData<DetalhesAction>()

    fun fetchReservas(name: String, name2: String) {
        viewModelScope.launch {
            when (val response = safeRequest { detalhesUseCase.fetchDetalhes(name) }) {
                is SafeResponse.Success -> {}
                is SafeResponse.GenericError -> DetalhesAction.Error(response.errorBody?.error)
                    .run()

                is SafeResponse.NetworkError -> DetalhesAction.Error().run()
            }
        }
    }

    fun fetchCurrentUserWithDetails() {
        val currentUser = firebaseHandler.getCurrentUser()
        if (currentUser != null) {
            firebaseHandler.fetchUserDetails(currentUser.uid) { user, error ->
                if (user != null) {
                    DetalhesAction.UserFetched(user).run()
                } else {
                    DetalhesAction.Error(error?.message ?: "Failed to fetch user details").run()
                }
            }
        } else {
            DetalhesAction.Error("User not logged in").run()
        }
    }

    private fun DetalhesAction.run() = detalhesAction.postValue(this)

}