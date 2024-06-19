package com.raul.quickcart.utils

import com.google.firebase.auth.FirebaseUser

// TODO: Objeto que almacena constantes utilizadas en la aplicación.
object Constant {
    const val urlDefaultImage = "url_user"
    var currentUser: FirebaseUser? = null
    var selectedLanguage: String = "-1"
}