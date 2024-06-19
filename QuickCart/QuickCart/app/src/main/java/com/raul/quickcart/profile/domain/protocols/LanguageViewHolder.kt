package com.raul.quickcart.profile.domain.protocols

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.profile.data.Language
import com.raul.quickcart.profile.presentation.LanguageFragment
import kotlinx.coroutines.flow.SharedFlow

// TODO: Clase abstracta para representar un ViewHolder de idiomas en un RecyclerView.
abstract class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // TODO: Método abstracto para enlazar datos del idioma a la vista.
    abstract fun bind(
        fragment: LanguageFragment,
        selectedLanguage: SharedFlow<String>,
        item: Language
    )

    // TODO: Método abstracto para manejar la selección de un idioma.
    abstract fun onSelectedLanguage(onClick: (language: Language) -> Unit)
}