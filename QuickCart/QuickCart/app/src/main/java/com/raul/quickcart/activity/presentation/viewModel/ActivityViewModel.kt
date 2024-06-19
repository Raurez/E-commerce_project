package com.raul.quickcart.activity.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.raul.quickcart.utils.Constant
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    // TODO: Función que verifica la autenticación del usuario.
    fun checkAuthentication(onFinish: () -> Unit) = viewModelScope.launch {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Constant.currentUser = currentUser
        onFinish()
    }
}
