package com.raul.quickcart.profile.presentation.adapterCard


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.raul.quickcart.databinding.ItemAddressBinding
import com.raul.quickcart.profile.data.Address


// TODO: Adaptador para la lista de direcciones.
class AddressAdapter(
    private val setDefaultAction: (Address) -> Unit,
    private val editAction: (Address) -> Unit,
    private val deleteAction: (Address) -> Unit
) : ListAdapter<Address, AddressViewHolder>(AddressDiffCallback()) {
    // TODO: Crea y retorna el ViewHolder para una dirección.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding, setDefaultAction, editAction, deleteAction)
    }

    // TODO: Enlaza los datos de una dirección con el ViewHolder.
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }
}









