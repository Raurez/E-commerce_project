package com.raul.quickcart.profile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.databinding.ViewholderLanguageBinding
import com.raul.quickcart.profile.data.Language
import com.raul.quickcart.profile.domain.protocols.LanguageViewHolder
import com.raul.quickcart.profile.presentation.LanguageFragment
import kotlinx.coroutines.flow.SharedFlow

// TODO: Adapter para un RecyclerView que muestra una lista de idiomas.
class LanguageAdapter(
    private val languageFragment: LanguageFragment,
    private val selectedLanguage: SharedFlow<String>,
    private val onSelectedLanguage: (Language) -> Unit
) : RecyclerView.Adapter<LanguageViewHolder>() {

    private var list: MutableList<Language> = mutableListOf()
    private var _binding: ViewholderLanguageBinding? = null
    private val binding get() = _binding!!

    // TODO: Crear un nuevo ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        _binding = ViewholderLanguageBinding.inflate(layoutInflater, parent, false)
        return LanguageViewHolderRow(binding)
    }

    // TODO: Obtener el número de elementos en la lista.
    override fun getItemCount(): Int = list.size

    // TODO: Vincular datos a la vista del ViewHolder.
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(fragment = languageFragment, selectedLanguage, list[position])
        // TODO: Acción al seleccionar un lenguaje.
        holder.onSelectedLanguage {
            onSelectedLanguage(it)
        }
    }

    // TODO: Establecer una nueva lista de lenguajes.
    fun setUpList(newList: MutableList<Language>) {
        val diffCallback = LanguageDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        list = newList
    }
}