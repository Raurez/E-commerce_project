package com.raul.quickcart.profile.presentation.adapterSearch

import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ViewholderSearchResultBinding
import com.raul.quickcart.utils.Utils

// TODO: ViewHolder para un elemento de resultado de búsqueda.
class SearchResultsViewHolder(
    private val binding: ViewholderSearchResultBinding,
    private val onItemClick: (Products) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // TODO: Método para enlazar los datos del producto con las vistas.
    fun bind(model: Products) {
        binding.apply {
            val language = Utils.getDeviceLanguage()
            val productName = if (language == "en") model.name_en else model.name

            textViewProductName.text = productName
            root.setOnClickListener {
                onItemClick(model)
            }
        }
    }
}