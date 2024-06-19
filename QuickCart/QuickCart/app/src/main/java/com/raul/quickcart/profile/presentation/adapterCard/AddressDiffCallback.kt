package com.raul.quickcart.profile.presentation.adapterCard

import androidx.recyclerview.widget.DiffUtil
import com.raul.quickcart.profile.data.Address

// TODO: Clase para calcular las diferencias entre dos listas de elementos de direcciones.
class AddressDiffCallback : DiffUtil.ItemCallback<Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem == newItem
    }
}
