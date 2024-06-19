package com.raul.quickcart.popularProducts.presentation.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.raul.quickcart.R
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.ViewholderPupListBinding
import com.raul.quickcart.utils.Utils
import com.raul.quickcart.utils.euros
import com.raul.quickcart.utils.toDecimalFormat

// TODO: ViewHolder para el adaptador de productos.
class ProductsViewHolder(
    private val binding: ViewholderPupListBinding,
    private val onItemClick: (Products) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(model: Products) {
        // TODO: Obtiene el idioma del dispositivo.
        val language = Utils.getDeviceLanguage()
        // TODO: Selecciona el nombre del producto según el idioma.
        val productName = if (language == "en") model.name_en else model.name


        // TODO: Configura las vistas con los datos del producto.
        binding.apply {
            imgPopu.load(model.image) {
                placeholder(R.drawable.logoredondo)
                error(R.drawable.logo2)
            }
            titleTxt.text = productName
            precioTxt.text = model.totalPrice.euros()
            scoreTxt.text = model.valoracion.toDecimalFormat()

            // TODO: Configura el click listener para la vista raíz.
            root.setOnClickListener {
                onItemClick(model)
            }
        }
    }
}

