package com.raul.quickcart.core.db

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.raul.quickcart.core.data.Category
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.core.data.documentToCategory
import com.raul.quickcart.core.data.documentToProducts
import com.raul.quickcart.utils.Utils

open class ProductsCloud {
    private val db = Firebase.firestore

    // TODO: Función para obtener la lista de todos los productos.
    fun list(onSuccess: (ArrayList<Products>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productsList = ArrayList<Products>()
                val categoryTasks = HashMap<String, Task<DocumentSnapshot>>()

                for (document in result) {
                    val product = documentToProducts(document)
                    product?.let {
                        productsList.add(it)
                        document.getDocumentReference("category")?.let { categoryRef ->
                            categoryTasks.putIfAbsent(categoryRef.id, categoryRef.get())
                        }
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(categoryTasks.values)
                    .addOnSuccessListener { categorySnapshots ->
                        val categoryMap = categorySnapshots.associateBy({ it.id },
                            { it.toObject(Category::class.java)?.name ?: "Default" })
                        productsList.forEach { product ->
                            product.categoryRef?.let { ref ->
                                product.categoryName = categoryMap[ref.id] ?: "Default"
                            }
                        }
                        Log.d("ProductsCloud", "Products loaded: $productsList")
                        onSuccess(productsList)
                    }.addOnFailureListener {
                        Log.e("ProductsCloud", "Error loading categories", it)
                        onError(it)
                    }
            }
            .addOnFailureListener {
                Log.e("ProductsCloud", "Error loading products", it)
                onError(it)
            }
    }

    // TODO: Función para obtener la lista de todos los productos populares.
    fun listPopular(onSuccess: (ArrayList<Products>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("products")
            .orderBy("valoracion", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val productsList = ArrayList<Products>()
                val categoryTasks = HashMap<String, Task<DocumentSnapshot>>()

                for (document in result) {
                    val product = documentToProducts(document)
                    product?.let {
                        productsList.add(it)
                        document.getDocumentReference("category")?.let { categoryRef ->
                            categoryTasks.putIfAbsent(categoryRef.id, categoryRef.get())
                        }
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(categoryTasks.values)
                    .addOnSuccessListener { categorySnapshots ->
                        val categoryMap = categorySnapshots.associateBy({ it.id },
                            { it.toObject(Category::class.java)?.name ?: "Default" })
                        productsList.forEach { product ->
                            product.categoryRef?.let { ref ->
                                product.categoryName = categoryMap[ref.id] ?: "Default"
                            }
                        }
                        onSuccess(productsList)
                    }.addOnFailureListener(onError)
            }
            .addOnFailureListener(onError)
    }

    // TODO: Función para obtener la lista de productos de una categoría específica.
    fun listByCategory(
        categoryName: String,
        onSuccess: (ArrayList<Products>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val language = Utils.getDeviceLanguage()
        val categoryField = if (language == "en") "name_en" else "name"

        db.collection("category").whereEqualTo(categoryField, categoryName).get()
            .addOnSuccessListener { categoryResult ->
                if (!categoryResult.isEmpty) {
                    val categoryDoc = categoryResult.documents[0]
                    val categoryRef = categoryDoc.reference
                    Log.d("ProductsCloud", "Category found: $categoryName")

                    db.collection("products").whereEqualTo("category", categoryRef).get()
                        .addOnSuccessListener { result ->
                            val productsList = ArrayList<Products>()
                            val categoryMap = mapOf(categoryRef.id to categoryName)
                            for (document in result) {
                                val product = documentToProducts(document, categoryMap)
                                product?.let {
                                    productsList.add(it)
                                }
                            }
                            Log.d(
                                "ProductsCloud",
                                "Products loaded for category $categoryName: $productsList"
                            )
                            onSuccess(productsList)
                        }
                        .addOnFailureListener {
                            Log.e(
                                "ProductsCloud",
                                "Error loading products for category $categoryName",
                                it
                            )
                            onError(it)
                        }
                } else {
                    Log.e("ProductsCloud", "Category not found: $categoryName")
                    onError(Exception("Category not found"))
                }
            }
            .addOnFailureListener {
                Log.e("ProductsCloud", "Error loading category $categoryName", it)
                onError(it)
            }
    }

    // TODO: Función para obtener la lista de todas las categorías disponibles.
    fun listCategories(onSuccess: (List<Category>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("category").get()
            .addOnSuccessListener { result ->
                val categories = result.mapNotNull { documentToCategory(it) }
                Log.d("ProductsCloud", "Available categories: $categories")
                onSuccess(categories)
            }
            .addOnFailureListener {
                Log.e("ProductsCloud", "Error loading categories", it)
                onError(it)
            }
    }
}











