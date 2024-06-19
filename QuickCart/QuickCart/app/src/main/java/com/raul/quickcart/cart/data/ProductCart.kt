package com.raul.quickcart.cart.data

import com.raul.quickcart.core.data.Products

// TODO: Clase que representa un producto en el carrito de compras.
data class ProductCart(
    val id: String,
    val name: String,
    val imageUrl: String,
    var quantity: Int,
    val pricePerUnit: Int,
    val discount: Double = 0.0,
    val tax: Double = 0.0,
    val delivery: Int = 350
) {
    // TODO: Precio del producto después del descuento.
    val priceAfterDiscount: Double
        get() = pricePerUnit - (pricePerUnit * discount)

    // TODO: Precio total del producto en el carrito.
    val totalPrice: Double
        get() = (priceAfterDiscount + (tax * pricePerUnit) + delivery) * quantity
}

// TODO: Función para crear un ProductCart a partir de un Products y una cantidad.
fun createProductCartFromProduct(product: Products, quantity: Int): ProductCart {
    return ProductCart(
        id = product.id,
        name = product.name,
        imageUrl = product.image,
        quantity = quantity,
        pricePerUnit = product.price,
        discount = product.discount.toDouble() / 100,
        tax = product.tax.toDouble() / 100
    )
}

