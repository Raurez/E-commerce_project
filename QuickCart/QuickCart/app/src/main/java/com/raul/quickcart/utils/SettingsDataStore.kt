package com.raul.quickcart.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raul.quickcart.aplication.QuickCartAplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES")

    // TODO: Método para guardar el idioma seleccionado.
    suspend fun saveStringValue(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    // TODO: Método para recuperar el idioma seleccionado.
    fun getStringValue(key: String): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }
    }

    // TODO: Método para guardar un valor de tipo Boolean en las preferencias de usuario.
    suspend fun saveBooleanValues(nameIndex: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(nameIndex)] = value
        }
    }

    // TODO: Método para recuperar un valor de tipo Boolean de las preferencias de usuario.
    fun getBooleanValue(nameIndex: String): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(nameIndex)] ?: false
        }
    }

    // TODO: Objeto compartido para acceder a las preferencias de usuario.
    companion object {
        @SuppressLint("StaticFieldLeak")
        val shared = SettingsDataStore(QuickCartAplication.appContext)
    }
}