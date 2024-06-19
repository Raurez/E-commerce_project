package com.raul.quickcart.auth.data.models


// TODO: Clase sellada que representa los diferentes estados de un recurso.
// TODO: Nota: Esta clase no se puede instanciar directamente.
sealed class ResourceState<out T> {
    // TODO: Estado de error que lleva un mensaje de error y un tipo opcional.
    data class Error<out T>(val msg: String, val type: T? = null) : ResourceState<T>()

    // TODO: Estado de éxito que lleva los datos del recurso.
    data class Success<out T>(val data: T) : ResourceState<T>()

    // TODO: Estado de carga, sin datos.
    object Loading : ResourceState<Nothing>()
}