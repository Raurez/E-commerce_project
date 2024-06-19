package com.raul.quickcart.home.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ProductsItemsBinding

// TODO: Adaptador para el carrusel de productos.
class CarruselAdapter(
    private val list: ArrayList<Products>,
    private val context: Context,
    private val onItemClick: (Products) -> Unit
) :
    RecyclerView.Adapter<CarruselViewHolder>() {

    // TODO: Crea y devuelve una nueva instancia de CarruselViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarruselViewHolder {
        val binding =
            ProductsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarruselViewHolder(binding, context, onItemClick)
    }

    // TODO: Vincula los datos del producto en la posición especificada con el ViewHolder.
    override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
        holder.bind(list[position])
    }

    // TODO: Devuelve el número total de elementos en el conjunto de datos.
    override fun getItemCount() = list.size

    // TODO: Actualiza la lista de productos con una nueva lista y notifica los cambios.
    fun updateList(newList: ArrayList<Products>) {
        val diffCallback = CarruselDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}

