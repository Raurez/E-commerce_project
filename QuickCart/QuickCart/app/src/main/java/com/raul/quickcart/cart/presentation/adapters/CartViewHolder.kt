package com.raul.quickcart.cart.presentation.adapters

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.raul.quickcart.cart.data.Action
import com.raul.quickcart.cart.data.ProductCart
import com.raul.quickcart.databinding.ViewholderCartBinding
import com.raul.quickcart.utils.euros
import com.raul.quickcart.utils.toPorcentText

// TODO: ViewHolder para los elementos del carrito de compras.
class CartViewHolder(
    private val binding: ViewholderCartBinding,
    private val onUpdate: (ProductCart, Action) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: ProductCart) {
        binding.imagenProducto.load(item.imageUrl) {
            transformations(RoundedCornersTransformation(10f))
        }
        binding.nameProducto.text = item.name
        binding.lnDiscount.isVisible = item.discount > 0

        if (binding.lnDiscount.isVisible) {
            binding.priceWithOutDiscount.text =
                ((item.pricePerUnit + (item.pricePerUnit * item.tax)) * item.quantity).euros()
            binding.discount.text = "-${(item.discount * 100).toPorcentText()}"
        }

        binding.precioProduct.text = item.pricePerUnit.toDouble().euros()
        binding.productosTotalAdd.text = item.quantity.toString()
        binding.precioTotal.text = item.totalPrice.euros()

        binding.btnAddProduct.setOnClickListener {
            onUpdate(item, Action.INCREASE)
        }

        binding.restarProductos.setOnClickListener {
            onUpdate(item, Action.DECREASE)
        }
    }
}







