package com.raul.quickcart.core.data

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

// TODO: Clase que representa un producto.

data class Products(
    var id: String = "",
    var name: String = "",
    var name_en: String = "",
    var description: String = "",
    var description_en: String = "",
    var price: Int = 0,
    @Exclude @set:Exclude @get:Exclude var categoryRef: DocumentReference? = null,
    var categoryName: String = "",
    var image: String = "",
    var discount: Int = 0,
    var tax: Int = 21,
    var valoracion: Int = 0
) {
    // TODO: Calcula el precio total del producto incluyendo impuestos.
    val totalPrice: Double
        get() {
            val amount = this.price.toDouble()
            val percent = this.tax.toDouble() / 100
            val subTotal = percent * amount
            return amount + subTotal
        }
}

// TODO: Función para convertir un documento de Firestore en un objeto Products.
fun documentToProducts(
    document: DocumentSnapshot,
    categoryMap: Map<String, String>? = null
): Products? {
    return if (document.exists()) {

        val product = Products(
            id = document.id,
            name = document.getString("name") ?: "",
            name_en = document.getString("name_en") ?: "",
            description = document.getString("descripcion") ?: "",
            description_en = document.getString("description_en") ?: "",
            price = (document.getLong("price") ?: 0L).toInt(),
            image = document.getString("image") ?: "",
            discount = (document.getLong("discuont") ?: 0L).toInt(),
            tax = (document.getLong("tax") ?: 0L).toInt(),
            valoracion = (document.getLong("valoracion") ?: 0L).toInt(),
            categoryRef = document.getDocumentReference("category")
        )

        categoryMap?.let {
            product.categoryRef?.let { ref ->
                product.categoryName = it[ref.id] ?: "Default"
            }
        }

        Log.d("documentToProducts", "Product: $product")
        product
    } else {
        null
    }
}








