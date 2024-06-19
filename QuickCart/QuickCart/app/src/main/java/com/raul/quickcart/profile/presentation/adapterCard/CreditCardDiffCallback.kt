package com.raul.quickcart.profile.presentation.adapterCard

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.profile.data.CreditCard

// TODO: Clase para calcular las diferencias entre dos listas de elementos de tarjeta de credito.
class CreditCardDiffCallback : DiffUtil.ItemCallback<CreditCard>() {
    override fun areItemsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
        return oldItem == newItem
    }
}
