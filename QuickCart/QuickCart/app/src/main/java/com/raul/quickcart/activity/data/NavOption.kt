package com.raul.quickcart.activity.data

import com.raul.quickcart.R

// TODO: Enum class que representa las opciones de navegación en la aplicación.
enum class NavOption (val rawValue: Int) {
    CART(R.id.navigation_cart),
    HOME(R.id.navigation_home),
    PROFILE(R.id.navigation_profile)
}