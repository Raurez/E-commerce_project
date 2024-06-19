package com.raul.quickcart.aplication

import android.app.Application
import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raul.quickcart.profile.domain.repositories.LanguagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Clase de la aplicación principal que extiende de Application.
class QuickCartAplication : Application() {
    // TODO: Variable para el servicio de repositorio de idiomas.
    private var service = LanguagesRepository()

    companion object {
        // TODO: Contexto de la aplicación accesible desde cualquier parte.
        lateinit var appContext: Context

        // TODO: Referencia a la base de datos de Firebase.
        lateinit var database: DatabaseReference
    }

    // TODO: Método que se llama cuando se crea la aplicación.
    override fun onCreate() {
        super.onCreate()
        // TODO: Inicializa el contexto de la aplicación.
        appContext = applicationContext
        // TODO: Lanza una corutina en el scope predeterminado para cargar los idiomas.
        CoroutineScope(Dispatchers.Default).launch {
            service.loadLanguages(this@QuickCartAplication)
        }
        // TODO: Inicializa la referencia a la base de datos de Firebase.
        database = Firebase.database.reference
    }
}