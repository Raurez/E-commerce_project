package com.raul.quickcart.cart.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raul.quickcart.core.db.FirebaseCloud
import com.raul.quickcart.profile.data.Address
import com.raul.quickcart.profile.data.CreditCard


// TODO: ViewModel para gestionar los datos del usuario.
class UserViewModel : ViewModel() {
    private val _defaultAddress = MutableLiveData<Address?>()
    val defaultAddress: LiveData<Address?> = _defaultAddress

    private val _defaultCreditCard = MutableLiveData<CreditCard?>()
    val defaultCreditCard: LiveData<CreditCard?> = _defaultCreditCard

    // TODO: Inicialización: carga los datos del usuario al crear el ViewModel.
    init {
        loadUserData()
    }

    // TODO: Método privado para cargar los datos del usuario.
    private fun loadUserData() {
        val currentUser = FirebaseCloud.users.getCurrentUser()
        if (currentUser != null) {
            FirebaseCloud.users.getUserData(currentUser.uid, onSuccess = { userData ->
                _defaultAddress.value = userData.addresses.find { it.isDefault }
                _defaultCreditCard.value = userData.creditCards.find { it.isDefault }
            }, onError = {
                _defaultAddress.value = null
                _defaultCreditCard.value = null
            })
        } else {
            _defaultAddress.value = null
            _defaultCreditCard.value = null
        }
    }

    // TODO: Método para recargar los datos del usuario.
    fun reloadUserData() {
        loadUserData()
    }
}

