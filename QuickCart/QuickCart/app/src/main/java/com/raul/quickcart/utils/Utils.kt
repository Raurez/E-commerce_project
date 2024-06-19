package com.raul.quickcart.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.raul.quickcart.R
import java.util.Locale

object Utils {

    // TODO: Función para mostrar un cuadro de diálogo de alerta.
    fun showAlert(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.title_acept), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    // TODO: Función para ocultar el teclado virtual en Android.
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        var view: View? = activity.currentFocus

        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // TODO: Función para obtener el idioma del dispositivo.
    fun getDeviceLanguage(): String {
        return Locale.getDefault().language
    }
}