package com.example.login.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.models.Login
import com.example.login.domain.LoginUseCase
import com.example.login.repository.readUserFromAssets
import com.example.login.view.action.LoginAction
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var loginAction = MutableLiveData<LoginAction>()
    var match = MutableLiveData<List<Login>>()

    fun login(user: String?, password: String?, context: Context?) {
        viewModelScope.launch {

            context?.readUserFromAssets()?.let {
                if (user == it.user && password == it.password)
                    LoginAction.LoginSuccess.run()
                else
                    LoginAction.Error("Usuario ou senha incorretos").run()
            } ?: LoginAction.Error().run()


            //TODO Real connection
//            when (val response = safeRequest { loginUseCase.login(user,password) }) {
//                is SafeResponse.Success -> {
//                    LoginAction.LoginSuccess.run()
//                }
//                is SafeResponse.GenericError -> LoginAction.Error(response.errorBody?.error).run()
//                is SafeResponse.NetworkError -> LoginAction.Error().run()
//                else -> {}
//            }
        }
    }

    private fun LoginAction.run() = loginAction.postValue(this)

}