package com.raul.quickcart.profile.presentation.adapterCard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.raul.quickcart.databinding.ItemCreditCardBinding
import com.raul.quickcart.profile.data.CreditCard


// TODO: Adaptador para la lista de tarjetas de crédito.
class CreditCardAdapter(
    private val setDefaultAction: (CreditCard) -> Unit,
    private val editAction: (CreditCard) -> Unit,
    private val deleteAction: (CreditCard) -> Unit
) : ListAdapter<CreditCard, CreditCardViewHolder>(CreditCardDiffCallback()) {

    // TODO: Crea y retorna el ViewHolder para una tarjeta de crédito.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val binding =
            ItemCreditCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditCardViewHolder(binding, setDefaultAction, editAction, deleteAction)
    }

    // TODO: Enlaza los datos de una tarjeta de crédito con el ViewHolder.
    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        val creditCard = getItem(position)
        holder.bind(creditCard)
    }
}









