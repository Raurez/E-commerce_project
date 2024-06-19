package com.raul.quickcart.home.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.core.data.Products

// TODO: Clase para calcular las diferencias entre dos listas de elementos del home.
class CarruselDiffCallback(
    private val oldList: List<Products>,
    private val newList: List<Products>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}