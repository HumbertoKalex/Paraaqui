package com.example.detalhes.plano.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detalhes.domain.DetalhesUseCase
import com.example.detalhes.plano.view.action.PlanoAction
import com.example.firebase.FirebaseHandler
import com.example.remote.SafeResponse
import com.example.remote.safeRequest
import kotlinx.coroutines.launch

class PlanoViewModel(
    private val detalhesUseCase: DetalhesUseCase,
    private val firebaseHandler: FirebaseHandler,
) : ViewModel() {

    var detalhesAction = MutableLiveData<PlanoAction>()

    fun fetchReservas(name: String, name2: String) {
        viewModelScope.launch {
            when (val response = safeRequest { detalhesUseCase.fetchDetalhes(name) }) {
                is SafeResponse.Success -> {}
                is SafeResponse.GenericError -> PlanoAction.Error(response.errorBody?.error).run()
                is SafeResponse.NetworkError -> PlanoAction.Error().run()
            }
        }
    }

    fun fetchCurrentUserWithDetails() {
        val currentUser = firebaseHandler.getCurrentUser()
        if (currentUser != null) {
            firebaseHandler.fetchUserDetails(currentUser.uid) { user, error ->
                if (user != null) {
                    PlanoAction.UserFetched(user).run()
                } else {
                    PlanoAction.Error(error?.message ?: "Failed to fetch user details").run()
                }
            }
        } else {
            PlanoAction.Error("User not logged in").run()
        }
    }

    fun updateUserPlan(planId: String) {
        val currentUser = firebaseHandler.getCurrentUser()
        if (currentUser != null) {
            firebaseHandler.updateUserPlan(planId) { result ->
                if (result.isSuccessful) {
                    PlanoAction.PlanoUpdated.run()
                } else {
                    PlanoAction.Error(result.exception?.message ?: "Failed to update plan").run()
                }
            }
        } else {
            PlanoAction.Error("User not logged in").run()
        }
    }

    private fun PlanoAction.run() = detalhesAction.postValue(this)
}
