package com.raul.quickcart.profile.presentation.adapterSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ViewholderSearchResultBinding

// TODO: Adaptador para los resultados de búsqueda.
class SearchResultsAdapter(private val onItemClick: (Products) -> Unit) :
    RecyclerView.Adapter<SearchResultsViewHolder>() {

    // TODO: Lista de productos que se mostrarán en el RecyclerView.
    private var products: List<Products> = emptyList()

    // TODO: Crea y retorna el ViewHolder para un elemento de producto.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderSearchResultBinding.inflate(inflater, parent, false)
        return SearchResultsViewHolder(binding, onItemClick)
    }

    // TODO: Enlaza los datos de un producto con el ViewHolder.
    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(products[position])
    }

    // TODO: Retorna el número de productos en la lista.
    override fun getItemCount() = products.size

    // TODO: Actualiza la lista de productos y notifica los cambios al adaptador.
    fun setProducts(newProducts: List<Products>) {
        val diffCallback = SearchResultsDiffCallback(this.products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.products = newProducts
        diffResult.dispatchUpdatesTo(this)
    }
}