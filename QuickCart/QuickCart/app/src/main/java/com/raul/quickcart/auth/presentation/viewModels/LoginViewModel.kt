package com.raul.quickcart.auth.presentation.viewModels

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.raul.quickcart.R
import com.raul.quickcart.auth.data.models.ErrorTypeValidLogin
import com.raul.quickcart.auth.data.models.ResourceState
import com.raul.quickcart.core.db.FirebaseCloud
import com.raul.quickcart.utils.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var _isValidForm: MutableSharedFlow<ArrayList<ResourceState<ErrorTypeValidLogin>>> =
        MutableSharedFlow()
    var isValidForm = _isValidForm.asSharedFlow()

    private var _loginResp = MutableSharedFlow<ResourceState<FirebaseUser?>>()
    var loginResp = _loginResp.asSharedFlow()

    // TODO: Método para iniciar sesión.
    fun login(email: String, password: String, context: Context) = viewModelScope.launch {
        _loginResp.emit(ResourceState.Loading)
        val resultValidate = validateForm(email, password, context)
        _isValidForm.emit(resultValidate)

        if (resultValidate.isEmpty()) {
            FirebaseCloud.users.login(email, password, onSuccess = {
                viewModelScope.launch {
                    Constant.currentUser = it
                    _loginResp.emit(ResourceState.Success(it))
                }
            }, onError = {
                viewModelScope.launch {
                    _loginResp.emit(
                        ResourceState.Error(
                            it ?: context.getString(R.string.title_Error)
                        )
                    )
                }
            })
        } else {
            _loginResp.emit(ResourceState.Error(context.getString(R.string.title_check_all)))
        }
    }

    // TODO: Método para validar el formulario.
    private fun validateForm(
        email: String,
        password: String,
        context: Context
    ): ArrayList<ResourceState<ErrorTypeValidLogin>> {

        val isValid: ArrayList<ResourceState<ErrorTypeValidLogin>> =
            arrayListOf()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid.add(
                ResourceState.Error(
                    context.getString(R.string.title_error_invalid_email),
                    ErrorTypeValidLogin.EMAIL
                )
            )
        }

        if (password.isEmpty() || password.length < 6) {

            isValid.add(
                ResourceState.Error(
                    context.getString(R.string.title_error_invalid_password),
                    ErrorTypeValidLogin.PASSWORD
                )
            )
        }

        return isValid
    }
}
