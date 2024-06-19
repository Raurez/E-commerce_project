package com.raul.quickcart.cart.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.R
import com.raul.quickcart.cart.data.Action
import com.raul.quickcart.cart.data.ProductCart
import com.raul.quickcart.databinding.ViewholderCartBinding

// TODO: Adaptador para el RecyclerView del carrito de compras.
class CartAdapter(private val onUpdate: (ProductCart, Action) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // TODO: Lista de elementos del carrito.
    private var items: MutableList<ProductCart> = mutableListOf()

    // TODO: Tipos de vista para el RecyclerView.
    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_EMPTY = 0
    }

    // TODO: Método para determinar el tipo de vista según el estado de los elementos.
    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    // TODO: Método para crear el ViewHolder según el tipo de vista.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CartViewHolder(binding, onUpdate)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_empty_cart, parent, false)
            EmptyViewHolder(view)
        }
    }

    // TODO: Método para enlazar datos a las vistas.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartViewHolder) {
            holder.bind(items[position])
        }
    }

    // TODO: Método para obtener la cantidad de elementos en el RecyclerView.
    override fun getItemCount(): Int = if (items.isEmpty()) 1 else items.size

    // TODO: Método para establecer los elementos del carrito en el adaptador.
    fun setItems(newItems: List<ProductCart>) {
        val diffCallback = CartDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    // TODO: Método para actualizar un elemento específico en el carrito.
    fun updateItem(item: ProductCart) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items[index] = item
            notifyItemChanged(index)
        }
    }

    // TODO: ViewHolder para el estado vacío del carrito.
    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


