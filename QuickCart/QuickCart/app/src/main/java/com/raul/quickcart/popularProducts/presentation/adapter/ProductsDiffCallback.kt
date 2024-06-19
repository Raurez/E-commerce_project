package com.raul.quickcart.popularProducts.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.core.data.Products

// TODO: Clase para calcular las diferencias entre dos listas de elementos de popularProducts.
class ProductsDiffCallback(
    private val oldList: List<Products>,
    private val newList: List<Products>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
