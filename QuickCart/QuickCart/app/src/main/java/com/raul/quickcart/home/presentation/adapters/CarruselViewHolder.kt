package com.raul.quickcart.home.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.raul.quickcart.R
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ProductsItemsBinding
import com.raul.quickcart.utils.Utils
import com.raul.quickcart.utils.euros
import com.raul.quickcart.utils.toDecimalFormat

// TODO: ViewHolder para el carrusel de productos.
class CarruselViewHolder(
    private val binding: ProductsItemsBinding,
    private val context: Context,
    private val onItemClick: (Products) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // TODO: Vincula los datos del producto al ViewHolder.
    @SuppressLint("SetTextI18n")
    fun bind(model: Products) {
        val language = Utils.getDeviceLanguage()
        val productName = if (language == "en") model.name_en else model.name

        binding.apply {
            imageCarruselItem.load(model.image) {
                placeholder(R.drawable.logoredondo)
                error(R.drawable.logo2)
            }
            titleTxt.text = productName
            feedTxt.text = model.totalPrice.euros()
            scoreTxt.text = model.valoracion.toDecimalFormat()

            // TODO: Configurar el clic en el elemento raíz para manejar la selección del producto.
            root.setOnClickListener {
                onItemClick(model)
            }
        }
    }
}


