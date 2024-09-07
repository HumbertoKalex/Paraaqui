package com.example.login.view.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.FirebaseHandler
import com.example.login.domain.LoginUseCase
import com.example.login.view.action.CreateAction
import com.example.login.view.action.LoginAction
import kotlinx.coroutines.launch

class CreateViewModel(
    private val loginUseCase: LoginUseCase,
    private val firebaseHandler: FirebaseHandler,
) : ViewModel() {

    var createAction = MutableLiveData<CreateAction>()

    fun create(email: String, password: String, nome: String, cpf: String) {
        viewModelScope.launch {
            firebaseHandler.createUser(email, password, nome, cpf) { task ->
                if (task.isSuccessful) {
                    CreateAction.CreateSuccess.run()
                } else {
                    CreateAction.Error("Informacoes incorretas").run()
                }
            }

        }
    }

    private fun CreateAction.run() = createAction.postValue(this)

}