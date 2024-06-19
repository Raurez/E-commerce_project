package com.raul.quickcart.core.db

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.raul.quickcart.R
import com.raul.quickcart.core.data.User

class UserCloud {

    private val db = FirebaseFirestore.getInstance()

    // TODO: Función para iniciar sesión con correo electrónico y contraseña.
    fun login(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String?) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(task.result.user)
                } else {
                    onError(task.exception?.message)
                }
            }
    }

    // TODO: Función para registrar un nuevo usuario con correo electrónico y contraseña.
    fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String?) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result.user
                    val user = User(
                        id = firebaseUser?.uid ?: "",
                        name = name,
                        email = email,
                        addresses = mutableListOf(),
                        creditCards = mutableListOf(),
                        favorites = mutableListOf()
                    )
                    firebaseUser?.let {
                        updateUserData(user, onSuccess = {
                            onSuccess(firebaseUser)
                        }, onError = onError)
                    }
                } else {
                    onError(task.exception?.message)
                }
            }
    }

    // TODO: Función para obtener los datos de un usuario desde Firestore.
    fun getUserData(userId: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                user?.let {
                    onSuccess(it)
                } ?: onError("User not found")
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error fetching user data")
            }
    }

    // TODO: Función para actualizar los datos de un usuario en Firestore.
    fun updateUserData(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = user.id
        val userRef = db.collection("users").document(userId)
        userRef.set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Unknown Error") }
    }

    // TODO: Función para obtener el usuario actualmente autenticado.
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    // TODO: Función para actualizar el perfil del usuario actual.
    fun updateUser(
        name: String,
        photoUrl: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            if (photoUrl.isNotEmpty()) {
                photoUri = Uri.parse(photoUrl)
            }
        }

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message)
            }
        }
    }

    // TODO: Función para restablecer la contraseña del usuario.
    fun resetPassword(
        email: String, context: Context, onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(context.getString(R.string.title_success))
                } else {
                    onError(task.exception?.message)
                }
            }
    }

    // TODO: Función para cerrar sesión del usuario actual.
    fun logOut(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        try {
            FirebaseAuth.getInstance().signOut()
            onSuccess()
        } catch (e: Exception) {
            onError(e.message)
        }
    }
}

