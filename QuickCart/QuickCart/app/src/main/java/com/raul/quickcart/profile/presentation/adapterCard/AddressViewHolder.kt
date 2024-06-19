package com.raul.quickcart.profile.presentation.adapterCard

import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.R
import com.raul.quickcart.databinding.ItemAddressBinding
import com.raul.quickcart.profile.data.Address

// TODO: ViewHolder para un elemento de dirección.
class AddressViewHolder(
    private val binding: ItemAddressBinding,
    private val setDefaultAction: (Address) -> Unit,
    private val editAction: (Address) -> Unit,
    private val deleteAction: (Address) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // TODO: Método para enlazar los datos de la dirección con las vistas.
    fun bind(address: Address) {
        binding.addressTextView.text = address.address
        binding.porDefectoAdrressIcon.setImageResource(
            if (address.isDefault) R.drawable.ic_star_full else R.drawable.ic_star_empty
        )
        binding.porDefectoAdrressIcon.setOnClickListener {
            setDefaultAction(address)
        }
        binding.editIcon.setOnClickListener { editAction(address) }
        binding.deleteIcon.setOnClickListener { deleteAction(address) }
    }
}


