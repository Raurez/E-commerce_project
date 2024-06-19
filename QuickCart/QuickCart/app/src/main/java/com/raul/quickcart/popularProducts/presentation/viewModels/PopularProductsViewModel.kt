package com.raul.quickcart.popularProducts.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.core.db.ProductsCloud
import com.raul.quickcart.utils.Utils

// TODO: ViewModel para productos populares.
class PopularProductsViewModel : ViewModel() {
    private val productsCloud = ProductsCloud()
    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> = _products

    private var allProducts: List<Products> = listOf()

    // TODO: Cargar todos los productos.
    fun loadProducts() {
        productsCloud.list(
            onSuccess = { productList ->
                Log.d("PopularProductsVM", "Products loaded: $productList")
                allProducts = productList
                _products.postValue(productList)
            },
            onError = { exception ->
                Log.e("PopularProductsVM", "Error loading products", exception)
            }
        )
    }

    // TODO: Cargar productos populares.
    private fun loadPopularProducts() {
        productsCloud.listPopular(
            onSuccess = { productList ->
                Log.d("PopularProductsVM", "Popular products loaded: $productList")
                allProducts = productList
                _products.postValue(productList)
            },
            onError = { exception ->
                Log.e("PopularProductsVM", "Error loading popular products", exception)
            }
        )
    }

    // TODO: Cargar productos por categoría.
    fun loadProductsByCategory(category: String, popularProductTitle: String, seeAllTitle: String) {
        when (category) {
            seeAllTitle -> {
                Log.d("PopularProductsVM", "Loading all products")
                loadProducts()
            }

            popularProductTitle -> {
                Log.d("PopularProductsVM", "Loading popular products")
                loadPopularProducts()
            }

            else -> {
                Log.d("PopularProductsVM", "Loading products for category: $category")
                productsCloud.listByCategory(category,
                    onSuccess = { productList ->
                        Log.d(
                            "PopularProductsVM",
                            "Products loaded for category $category: $productList"
                        )
                        allProducts = productList
                        _products.postValue(productList)
                    },
                    onError = { exception ->
                        Log.e(
                            "PopularProductsVM",
                            "Error loading products for category $category",
                            exception
                        )
                    }
                )
            }
        }
    }

    // TODO: Filtrar productos según criterio.
    fun filterProducts(filter: String) {
        val productsList = _products.value ?: emptyList()
        val language = Utils.getDeviceLanguage()
        val filteredList = when (filter) {
            "Alphabetically" -> {
                val sortedList = if (language == "en") {
                    productsList.sortedBy { it.name_en.trim().ifEmpty { it.name.trim() } }
                } else {
                    productsList.sortedBy { it.name.trim().ifEmpty { it.name_en.trim() } }
                }
                Log.d("PopularProductsVM", "Sorted Alphabetically ($language): $sortedList")
                sortedList
            }

            "Popular Product" -> {
                val sortedList = productsList.sortedByDescending { it.valoracion }
                Log.d("PopularProductsVM", "Sorted by Popularity: $sortedList")
                sortedList
            }

            "Cheaper Product" -> {
                val sortedList = productsList.sortedBy { it.totalPrice }
                Log.d("PopularProductsVM", "Sorted by Price: $sortedList")
                sortedList
            }

            else -> productsList
        }
        _products.postValue(filteredList)
    }

    // TODO: Buscar productos según una consulta.
    fun searchProducts(query: String) {
        val language = Utils.getDeviceLanguage()
        val searchList = allProducts.filter {
            val nameToSearch = if (language == "en") it.name_en else it.name
            nameToSearch.contains(query, ignoreCase = true)
        }
        _products.postValue(searchList)
    }
}
















