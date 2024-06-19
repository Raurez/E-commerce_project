package com.raul.quickcart.profile.presentation.viewModels

import android.content.Context
import android.os.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raul.quickcart.profile.data.Language
import com.raul.quickcart.utils.Constant
import com.raul.quickcart.utils.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

// TODO: ViewModel para gestionar la selección de idiomas.
class LanguageViewModel : ViewModel() {
    private val _selectedLanguage: MutableStateFlow<String> = MutableStateFlow(Constant.selectedLanguage)
    val selectedLanguage = _selectedLanguage.asSharedFlow()
    private var defaultLanguage = ""

    // TODO: Función para seleccionar un idioma.
    fun selectLanguage(context: Context, l: Language) = viewModelScope.launch {

        if (defaultLanguage.isEmpty())
            defaultLanguage = context.resources.configuration.locales[0].language

        val language = if (l.value != "-1")
            l.value
        else
            defaultLanguage

        Constant.selectedLanguage = language

        val locale = Locale(language)

        val res = context.resources
        val configuration = res.configuration

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)

        context.createConfigurationContext(configuration)

        SettingsDataStore.shared.saveStringValue("selected_language", language)

        _selectedLanguage.emit(l.value)
    }

    // TODO: Método para cargar el idioma guardado.
    fun loadSelectedLanguage(context: Context) = viewModelScope.launch {
        SettingsDataStore.shared.getStringValue("selected_language").collectLatest { language ->
            if (language.isNotEmpty()) {
                val locale = Locale(language)
                val res = context.resources
                val configuration = res.configuration

                val localeList = LocaleList(locale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)

                context.createConfigurationContext(configuration)

                _selectedLanguage.emit(language)
            }
        }
    }
}