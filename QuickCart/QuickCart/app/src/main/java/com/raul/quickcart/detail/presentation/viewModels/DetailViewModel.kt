package com.raul.quickcart.detail.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.core.data.documentToProducts
import com.raul.quickcart.utils.Constant

// TODO: ViewModel para gestionar los detalles del producto.
class DetailViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _productDetails = MutableLiveData<Products>()
    val productDetails: LiveData<Products> = _productDetails
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var productId: String? = null

    // Función para cargar los detalles del producto.
    fun loadProductDetails(productId: String) {
        this.productId = productId
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val product = documentToProducts(documentSnapshot)
                product?.let {
                    Log.d("DetailViewModel", "Loaded product: $product")
                    _productDetails.postValue(it)
                    checkIfFavorite(productId)
                } ?: run {
                    Log.e("DetailViewModel", "Product document is null")
                }
            }
            .addOnFailureListener {
                Log.e("DetailViewModel", "Error loading product details", it)
            }
    }

    // TODO: Función para verificar si el producto está marcado como favorito por el usuario actual.
    private fun checkIfFavorite(productId: String) {
        val user = Constant.currentUser
        user?.let {
            db.collection("users").document(it.uid).collection("favorites").document(productId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    _isFavorite.postValue(documentSnapshot.exists())
                }
                .addOnFailureListener {
                    Log.e("DetailViewModel", "Error checking favorite status", it)
                }
        }
    }

    // TODO: Función para alternar el estado de favorito del producto.
    fun toggleFavorite() {
        val user = Constant.currentUser
        val currentProductId = productId ?: return

        user?.let {
            val userFavoritesRef = db.collection("users").document(it.uid).collection("favorites")
                .document(currentProductId)
            _isFavorite.value?.let { isFavorite ->
                if (isFavorite) {
                    userFavoritesRef.delete()
                        .addOnSuccessListener {
                            _isFavorite.postValue(false)
                        }
                        .addOnFailureListener {
                            Log.e("DetailViewModel", "Error removing from favorites", it)
                        }
                } else {
                    userFavoritesRef.set(mapOf("productId" to currentProductId))
                        .addOnSuccessListener {
                            _isFavorite.postValue(true)
                        }
                        .addOnFailureListener {
                            Log.e("DetailViewModel", "Error adding to favorites", it)
                        }
                }
            }
        }
    }
}







