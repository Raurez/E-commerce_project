package com.raul.quickcart.cart.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.cart.data.ProductCart

// TODO: Clase para calcular las diferencias entre dos listas de elementos del carrito.
class CartDiffCallback(
    private val oldList: List<ProductCart>,
    private val newList: List<ProductCart>
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
