package com.raul.quickcart.profile.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raul.quickcart.core.data.User
import com.raul.quickcart.core.db.FirebaseCloud
import com.raul.quickcart.profile.data.Address
import com.raul.quickcart.profile.data.CreditCard

// TODO: ViewModel para gestionar el área privada del usuario.
class PrivateAreaViewModel : ViewModel() {
    // TODO: LiveData para la lista de direcciones del usuario.
    private val _addressList = MutableLiveData<List<Address>>()
    val addressList: LiveData<List<Address>> = _addressList

    // TODO: LiveData para la lista de tarjetas de crédito del usuario.
    private val _creditCardList = MutableLiveData<List<CreditCard>>()
    val creditCardList: LiveData<List<CreditCard>> = _creditCardList

    // TODO: LiveData para el usuario actual.
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    // TODO: Método para cargar los datos del usuario actual desde Firebase.
    fun loadUser() {
        FirebaseCloud.users.getCurrentUser()?.let { user ->
            FirebaseCloud.users.getUserData(user.uid, onSuccess = { userData ->
                _currentUser.value = userData
                _addressList.value = userData.addresses
                _creditCardList.value = userData.creditCards
            }, onError = {
                _currentUser.value = null
            })
        } ?: run {
            _currentUser.value = null
        }
    }

    // TODO: Método para guardar una nueva dirección.
    fun saveAddress(address: Address, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedAddresses = user.addresses.toMutableList().apply { add(address) }
            val updatedUser = user.copy(addresses = updatedAddresses)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _addressList.value = updatedAddresses
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para guardar una nueva tarjeta de crédito.
    fun saveCreditCard(creditCard: CreditCard, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedCreditCards = user.creditCards.toMutableList().apply { add(creditCard) }
            val updatedUser = user.copy(creditCards = updatedCreditCards)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _creditCardList.value = updatedCreditCards
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para actualizar una dirección existente.
    fun updateAddress(address: Address, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedAddresses = user.addresses.toMutableList().apply {
                val index = indexOfFirst { it.id == address.id }
                if (index != -1) {
                    set(index, address)
                }
            }
            val updatedUser = user.copy(addresses = updatedAddresses)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _addressList.value = updatedAddresses
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para actualizar una tarjeta de crédito existente.
    fun updateCreditCard(creditCard: CreditCard, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedCreditCards = user.creditCards.toMutableList().apply {
                val index = indexOfFirst { it.id == creditCard.id }
                if (index != -1) {
                    set(index, creditCard)
                }
            }
            val updatedUser = user.copy(creditCards = updatedCreditCards)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _creditCardList.value = updatedCreditCards
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para eliminar una dirección.
    fun deleteAddress(address: Address, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedAddresses = user.addresses.toMutableList().apply {
                removeAll { it.id == address.id }
            }
            val updatedUser = user.copy(addresses = updatedAddresses)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _addressList.value = updatedAddresses
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para eliminar una tarjeta de crédito.
    fun deleteCreditCard(creditCard: CreditCard, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedCreditCards = user.creditCards.toMutableList().apply {
                removeAll { it.id == creditCard.id }
            }
            val updatedUser = user.copy(creditCards = updatedCreditCards)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _creditCardList.value = updatedCreditCards
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para establecer una dirección como predeterminada.
    fun setDefaultAddress(address: Address, onSuccess: () -> Unit, onError: (String) -> Unit) {
        currentUser.value?.let { user ->
            val updatedAddresses =
                user.addresses.map { it.copy(isDefault = it.id == address.id) }.toMutableList()
            val updatedUser = user.copy(addresses = updatedAddresses)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _addressList.value = updatedAddresses
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }

    // TODO: Método para establecer una tarjeta de crédito como predeterminada.
    fun setDefaultCreditCard(
        creditCard: CreditCard,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        currentUser.value?.let { user ->
            val updatedCreditCards =
                user.creditCards.map { it.copy(isDefault = it.id == creditCard.id) }.toMutableList()
            val updatedUser = user.copy(creditCards = updatedCreditCards)
            FirebaseCloud.users.updateUserData(updatedUser, onSuccess = {
                _currentUser.value = updatedUser
                _creditCardList.value = updatedCreditCards
                onSuccess()
            }, onError = onError)
        } ?: onError("User not logged in")
    }
}













