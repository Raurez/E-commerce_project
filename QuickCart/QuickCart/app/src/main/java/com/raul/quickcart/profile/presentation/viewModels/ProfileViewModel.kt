package com.raul.quickcart.profile.presentation.viewModels

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raul.quickcart.R
import com.raul.quickcart.auth.data.models.ResourceState
import com.raul.quickcart.core.data.User
import com.raul.quickcart.core.db.FirebaseCloud
import com.raul.quickcart.utils.Constant
import com.raul.quickcart.utils.SettingsDataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

// TODO: ViewModel para gestionar el perfil del usuario.
class ProfileViewModel : ViewModel() {

    // TODO: LiveData para almacenar los datos del usuario.
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    // TODO: MutableSharedFlow para emitir el resultado del cierre de sesión.
    private val _logoutResult = MutableSharedFlow<ResourceState<Boolean>>()
    val logoutResult = _logoutResult.asSharedFlow()

    // TODO: Método para cargar los datos del usuario desde Firebase.
    fun loadUserData(userId: String) {
        FirebaseCloud.users.getUserData(userId, onSuccess = {
            _userData.postValue(it)
        }, onError = {
            // Todo: Error
        })
    }

    // TODO: Método para actualizar los datos del usuario en Firebase.
    fun updateUserData(user: User) {
        FirebaseCloud.users.updateUserData(user, onSuccess = {
            _userData.postValue(user)
        }, onError = {
            // Todo: Error
        })
    }

    // TODO: Método para cerrar sesión del usuario.
    fun logOut() = viewModelScope.launch {
        _logoutResult.emit(ResourceState.Loading)
        FirebaseCloud.users.logOut({
            viewModelScope.launch {
                Constant.currentUser = null
                _logoutResult.emit(ResourceState.Success(true))
            }
        }) { error ->
            viewModelScope.launch {
                _logoutResult.emit(ResourceState.Error(error ?: "Unknown Error"))
            }
        }
    }

    // TODO: Método para cambiar el icono y texto del selector de modo oscuro.
    fun changeStateDarkModeSelector(isDarkMode: Boolean, icon: ImageView, textElement: TextView) =
        viewModelScope.launch {
            if (isDarkMode) {
                icon.setImageResource(R.drawable.profile_dark_mode)
                textElement.text = textElement.context.getString(R.string.title_dark_mode)
            } else {
                icon.setImageResource(R.drawable.profile_light_mode)
                textElement.text = textElement.context.getString(R.string.title_mode_light)
            }
        }

    // TODO: Método para aplicar el tema seleccionado y guardar la preferencia.
    fun applyTheme(isDarkModeEnabled: Boolean, icon: ImageView, textElement: TextView) =
        viewModelScope.launch {
            SettingsDataStore.shared.saveBooleanValues("dark mode", isDarkModeEnabled)
            changeStateDarkModeSelector(isDarkModeEnabled, icon, textElement)
        }
}

