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
import com.raul.quickcart.utils.Utils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    private var _isValidForm: MutableStateFlow<ArrayList<ResourceState<ErrorTypeValidLogin>>> =
        MutableStateFlow(
            arrayListOf()
        )
    var isValidForm = _isValidForm.asSharedFlow()

    private var _resetPassword = MutableSharedFlow<ResourceState<FirebaseUser?>>()
    var resetPassword = _resetPassword.asSharedFlow()

    // TODO: Método para restablecer la contraseña.
    fun resetPassword(email: String, context: Context) = viewModelScope.launch {
        _resetPassword.emit(ResourceState.Loading)
        _isValidForm.emit(validateForm(email, context))

        if (_isValidForm.value.isEmpty()) {
            FirebaseCloud.users.resetPassword(email, context, onSuccess = { message ->
                viewModelScope.launch {
                    Utils.showAlert(
                        context,
                        context.getString(R.string.title_success_add),
                        context.getString(R.string.title_success)
                    )
                    _resetPassword.emit(ResourceState.Success(null))
                }

            }, onError = { error ->
                viewModelScope.launch {
                    _resetPassword.emit(
                        ResourceState.Error(
                            error ?: context.getString(R.string.title_Error)
                        )
                    )
                }
            })
        } else {
            _resetPassword.emit(ResourceState.Error(context.getString(R.string.title_check_all)))
        }
    }

    // TODO: Método para validar el formulario.
    private fun validateForm(
        email: String,
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

        return isValid
    }
}