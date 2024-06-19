package com.raul.quickcart.utils

import android.annotation.SuppressLint
import com.raul.quickcart.R
import com.raul.quickcart.aplication.QuickCartAplication
import kotlin.math.floor

// TODO: Extensión para convertir un valor Double a euros.
fun Double.euros(): String {
    val currencyEuros = this / 100
    return QuickCartAplication.appContext.getString(
        R.string.currency,
        currencyEuros.truncateTwoDecimals()
    )
}

// TODO: Extensión para formatear un entero en formato decimal con un solo decimal.
@SuppressLint("DefaultLocale")
fun Int.toDecimalFormat(): String {
    return String.format("%.1f", this / 10.0)
}

// TODO: Extensión para formatear un Double como texto con un porcentaje.
@SuppressLint("DefaultLocale")
fun Double.toPorcentText(): String {
    return String.format("%.0f", this) + "%"
}

// TODO: Función para truncar un Double a dos decimales.
fun Double.truncateTwoDecimals(): Double {
    return floor(this * 100) / 100
}
