package com.raul.quickcart.home.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.core.db.FirebaseCloud
import java.util.Locale

class HomeViewModel : ViewModel() {
    private val productsCloud = FirebaseCloud.products
    private val _products = MutableLiveData<ArrayList<Products>>()
    val products: LiveData<ArrayList<Products>> = _products

    private val _searchResults = MutableLiveData<List<Products>>()
    val searchResults: LiveData<List<Products>> = _searchResults

    // TODO: Carga la lista de productos populares desde Firebase.
    fun loadProducts() {
        productsCloud.listPopular(
            onSuccess = { productList ->
                _products.postValue(productList)
            },
            onError = { exception ->
                Log.e("HomeViewModel", "Error loading products", exception)
            }
        )
    }

    // TODO: Busca productos en la lista cargada basándose en una consulta.
    fun searchProducts(query: String) {
        val language = Locale.getDefault().language
        val filteredList = _products.value?.filter {
            val name = if (language == "en") it.name_en else it.name
            name.contains(query, ignoreCase = true)
        } ?: emptyList()
        _searchResults.postValue(filteredList)
    }
}


