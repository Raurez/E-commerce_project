package com.raul.quickcart.core.data

import com.raul.quickcart.profile.data.Address
import com.raul.quickcart.profile.data.CreditCard

// TODO: Clase que representa un usuario.
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val darkMode: Boolean = false,
    val favorites: MutableList<String> = mutableListOf(),
    val addresses: MutableList<Address> = mutableListOf(),
    val creditCards: MutableList<CreditCard> = mutableListOf(),
    val language: String = ""
)




