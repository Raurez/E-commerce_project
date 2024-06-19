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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private var _isValidForm: MutableStateFlow<ArrayList<ResourceState<ErrorTypeValidLogin>>> =
        MutableStateFlow(
            arrayListOf()
        )
    var isValidForm = _isValidForm.asStateFlow()

    private var _registerResp = MutableSharedFlow<ResourceState<FirebaseUser?>>()
    var registerResp = _registerResp.asSharedFlow()

    // TODO: Método para registrarse.
    fun register(
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
        context: Context
    ) = viewModelScope.launch {
        _registerResp.emit(ResourceState.Loading)
        _isValidForm.emit(validateForm(name, email, password, repeatPassword, phone, context))

        if (_isValidForm.value.isEmpty()) {
            FirebaseCloud.users.register(name, email, password, phone, onSuccess = { user ->
                viewModelScope.launch {
                    Constant.currentUser = user
                    FirebaseCloud.users.updateUser(name = name, photoUrl = "", onSuccess = {
                        viewModelScope.launch {
                            _registerResp.emit(ResourceState.Success(user))
                        }

                    }, onError = {
                        viewModelScope.launch {
                            Constant.currentUser = null
                            _registerResp.emit(
                                ResourceState.Error(
                                    it ?: context.getString(R.string.title_Error)
                                )
                            )
                        }
                    })
                }

            }, onError = {
                viewModelScope.launch {
                    _registerResp.emit(
                        ResourceState.Error(
                            it ?: context.getString(R.string.title_Error)
                        )
                    )
                }
            })
        } else {
            _registerResp.emit(ResourceState.Error(context.getString(R.string.title_check_all)))
        }
    }

    // TODO: Método para validar el formulario.
    private fun validateForm(
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
        phone: String,
        context: Context
    ): ArrayList<ResourceState<ErrorTypeValidLogin>> {
        val isValid: ArrayList<ResourceState<ErrorTypeValidLogin>> = arrayListOf()

        if (name.isEmpty()) {
            isValid.add(
                ResourceState.Error(
                    context.getString(R.string.title_error_name_required),
                    ErrorTypeValidLogin.NAME
                )
            )
        }

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

        if (!repeatPassword.equals(password)) {
            isValid.add(
                ResourceState.Error(
                    context.getString(R.string.title_error_password_mismatch),
                    ErrorTypeValidLogin.REPEAT_PASSWORD
                )
            )
        }

        if (phone.isEmpty() || phone.length < 9) {
            isValid.add(
                ResourceState.Error(
                    context.getString(R.string.title_error_phone_required),
                    ErrorTypeValidLogin.PHONE
                )
            )
        }

        return isValid
    }
}