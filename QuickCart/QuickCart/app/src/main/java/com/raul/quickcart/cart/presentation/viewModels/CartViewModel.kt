package com.raul.quickcart.cart.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raul.quickcart.cart.data.ProductCart

// TODO: ViewModel para gestionar el carrito de compras.
class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<ProductCart>>()
    val cartItems: LiveData<List<ProductCart>> = _cartItems
    private val _updatedProduct = MutableLiveData<ProductCart>()
    val updatedProduct: LiveData<ProductCart> = _updatedProduct

    private val cartRepository = mutableListOf<ProductCart>()

    // TODO: Método para agregar un producto al carrito.
    fun addToCart(productCart: ProductCart) {
        val existingProduct = cartRepository.find { it.id == productCart.id }
        if (existingProduct != null) {
            existingProduct.quantity += productCart.quantity
        } else {
            cartRepository.add(productCart)
        }
        _cartItems.value = cartRepository.toList()
    }

    // TODO: Método para aumentar la cantidad de un producto en el carrito.
    fun increaseProductQuantity(updatedProduct: ProductCart) {
        val index = cartRepository.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            cartRepository[index].quantity++
            _cartItems.value = cartRepository.toList()
            _updatedProduct.value = cartRepository[index]
        }
    }

    // TODO: Método para disminuir la cantidad de un producto en el carrito.
    fun decreaseProductQuantity(updatedProduct: ProductCart) {
        val index = cartRepository.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            if (cartRepository[index].quantity > 1) {
                cartRepository[index].quantity--
                _updatedProduct.value = cartRepository[index]
            } else {
                cartRepository.removeAt(index)
            }
            _cartItems.value = cartRepository.toList()
        }
    }

    // TODO: Método para limpiar el carrito.
    fun clearCart() {
        cartRepository.clear()
        _cartItems.value = cartRepository.toList()
    }
}











