package com.raul.quickcart.profile.presentation.adapter

import androidx.lifecycle.lifecycleScope
import com.raul.quickcart.databinding.ViewholderLanguageBinding
import com.raul.quickcart.profile.data.Language
import com.raul.quickcart.profile.domain.protocols.LanguageViewHolder
import com.raul.quickcart.profile.presentation.LanguageFragment
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Clase que representa el ViewHolder para un elemento de lenguaje.
class LanguageViewHolderRow(val binding: ViewholderLanguageBinding) :
    LanguageViewHolder(binding.root) {
    // TODO: Variable privada para almacenar el lenguaje actual.
    private var language: Language? = null

    // TODO: Método para enlazar los datos con la vista.
    override fun bind(
        fragment: LanguageFragment,
        selectedLanguage: SharedFlow<String>,
        item: Language
    ) {
        language = item
        binding.ivIcon.setImageResource(item.iconId)
        binding.title.text = fragment.getString(item.nameLanguageId)

        fragment.lifecycleScope.launch {
            selectedLanguage.collectLatest {
                binding.card.isChecked = item.value == it
            }
        }
    }

    // TODO: Método para manejar la selección del lenguaje.
    override fun onSelectedLanguage(onClick: (language: Language) -> Unit) {
        binding.card.setOnClickListener {
            if (language != null)
                onClick(language!!)
        }
    }
}
