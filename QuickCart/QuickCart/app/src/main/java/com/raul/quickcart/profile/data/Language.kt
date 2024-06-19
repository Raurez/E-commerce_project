package com.raul.quickcart.profile.data

import java.util.UUID

// TODO: Clase de datos para representar un idioma
data class Language(
    val id: UUID = UUID.randomUUID(),
    var nameLanguageId: Int,
    var iconId: Int,
    var value: String
)
