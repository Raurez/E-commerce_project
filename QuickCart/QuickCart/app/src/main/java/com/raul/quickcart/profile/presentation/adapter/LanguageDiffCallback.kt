package com.raul.quickcart.profile.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.profile.data.Language

// TODO: Clase para calcular las diferencias entre dos listas de elementos de idiomas.
class LanguageDiffCallback(
    private val oldList: MutableList<Language>,
    private val newList: MutableList<Language>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
