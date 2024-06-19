package com.raul.quickcart.auth.data.models

// TODO:  Enum class que representa los tipos de error al validar un inicio de sesión.
enum class ErrorTypeValidLogin {
    PASSWORD,
    EMAIL,
    NOTHING,
    NAME,
    REPEAT_PASSWORD,
    PHONE
}