package com.raul.quickcart.profile.domain.repositories

import android.content.Context
import com.raul.quickcart.R
import com.raul.quickcart.profile.data.Language
import com.raul.quickcart.utils.Data

class LanguagesRepository {

    // TODO: Función para cargar la lista de idiomas en la aplicación.
    fun loadLanguages(context: Context) {
        // TODO: Limpiar la lista de idiomas antes de agregar nuevos elementos.
        Data.listLanguage.clear()
        // TODO: Agregar los idiomas disponibles a la lista de idiomas.
        Data.listLanguage.addAll(
            listOf(
                Language(
                    iconId = R.drawable.profile_languaje,
                    nameLanguageId = R.string.title_system,
                    value = "-1"
                ),
                Language(
                    iconId = R.drawable.spain,
                    nameLanguageId = R.string.title_Spanish,
                    value = "es"
                ),
                Language(
                    iconId = R.drawable.english,
                    nameLanguageId = R.string.title_English,
                    value = "en"
                )
            )
        )
    }
}