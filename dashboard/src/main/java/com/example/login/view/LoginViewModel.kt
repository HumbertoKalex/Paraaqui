package com.example.login.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.FirebaseHandler
import com.example.login.domain.LoginUseCase
import com.example.login.repository.readUserFromAssets
import com.example.login.view.action.LoginAction
import kotlinx.coroutines.launch

class LoginViewModel(
    private val firebaseHandler: FirebaseHandler,
) : ViewModel() {

    var loginAction = MutableLiveData<LoginAction>()

    fun login(user: String, password: String) {
        viewModelScope.launch {
            if(user.isNotEmpty()) {
                firebaseHandler.loginUser(user, password) { task ->
                    if (task.isSuccessful) {
                        LoginAction.LoginSuccess.run()
                    } else {
                        LoginAction.Error("Usuario ou senha incorretos").run()
                    }
                }
            }else{
                LoginAction.Error("Usuario ou senha incorretos").run()
            }

        }
    }


    private fun LoginAction.run() = loginAction.postValue(this)

}