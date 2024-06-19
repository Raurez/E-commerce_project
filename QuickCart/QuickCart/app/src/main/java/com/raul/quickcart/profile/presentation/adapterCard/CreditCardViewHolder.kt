package com.raul.quickcart.profile.presentation.adapterCard

import androidx.recyclerview.widget.RecyclerView
import com.raul.quickcart.R
import com.raul.quickcart.databinding.ItemCreditCardBinding
import com.raul.quickcart.profile.data.CreditCard


// TODO: ViewHolder para un elemento de tarjeta de crédito.
class CreditCardViewHolder(
    private val binding: ItemCreditCardBinding,
    private val setDefaultAction: (CreditCard) -> Unit,
    private val editAction: (CreditCard) -> Unit,
    private val deleteAction: (CreditCard) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // TODO: Método para enlazar los datos de la tarjeta de crédito con las vistas.
    fun bind(creditCard: CreditCard) {
        binding.creditCardTextView.text = maskCardNumber(creditCard.number)
        binding.porDefectoCardIcon.setImageResource(
            if (creditCard.isDefault) R.drawable.ic_star_full else R.drawable.ic_star_empty
        )
        binding.porDefectoCardIcon.setOnClickListener {
            setDefaultAction(creditCard)
        }
        binding.editIcon.setOnClickListener { editAction(creditCard) }
        binding.deleteIcon.setOnClickListener { deleteAction(creditCard) }
    }

    // TODO: Método para enmascarar el número de la tarjeta.
    private fun maskCardNumber(cardNumber: String): String {
        return if (cardNumber.length > 4) {
            "**** **** **** " + cardNumber.takeLast(4)
        } else {
            cardNumber
        }
    }
}



