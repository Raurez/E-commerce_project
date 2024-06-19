package com.raul.quickcart.core.data

import com.google.firebase.firestore.DocumentSnapshot

// TODO: Clase que representa una categoría de productos.
data class Category(
    val name: String = "",
    val image: String = "",
    var name_en: String = ""
)

// TODO: Función para convertir un documento de Firestore en un objeto Category.
fun documentToCategory(document: DocumentSnapshot): Category? {
    return if (document.exists()) {
        Category(
            name = document.getString("name") ?: "",
            image = document.getString("image") ?: "",
            name_en = document.getString("name_en") ?: ""
        )
    } else {
        null
    }
}


