package com.raul.quickcart.popularProducts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ViewholderPupListBinding

// TODO: Adapter para un RecyclerView que muestra una lista de productos.
class ProductsAdapter(private val onItemClick: (Products) -> Unit) :
    RecyclerView.Adapter<ProductsViewHolder>() {
    // Lista de productos a mostrar.
    private var products: List<Products> = emptyList()

    // TODO: Crea e infla la vista del ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderPupListBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding, onItemClick)
    }

    // TODO: Vincula los datos del producto a la vista del ViewHolder.
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(products[position])
    }

    // TODO: Retorna el tamaño de la lista de productos.
    override fun getItemCount() = products.size

    // TODO: Actualiza la lista de productos en el adaptador.
    fun setProducts(newProducts: List<Products>) {
        val diffCallback = ProductsDiffCallback(this.products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.products = newProducts
        diffResult.dispatchUpdatesTo(this)
    }
}



