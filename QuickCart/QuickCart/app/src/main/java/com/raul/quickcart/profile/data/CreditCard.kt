package com.raul.quickcart.profile.data

// TODO: Clase de datos para representar una tarjeta de crédito.
data class CreditCard(
    val id: String = "",
    val number: String = "",
    val expiry: String = "",
    val cvv: String = "",
    var isDefault: Boolean = false
)


