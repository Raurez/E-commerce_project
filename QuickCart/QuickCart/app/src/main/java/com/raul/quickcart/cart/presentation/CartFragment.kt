package com.raul.quickcart.cart.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raul.quickcart.R
import com.raul.quickcart.cart.data.Action
import com.raul.quickcart.cart.data.ProductCart
import com.raul.quickcart.cart.presentation.adapters.CartAdapter
import com.raul.quickcart.cart.presentation.viewModels.CartViewModel
import com.raul.quickcart.cart.presentation.viewModels.UserViewModel
import com.raul.quickcart.databinding.FragmentCartBinding
import com.raul.quickcart.utils.Constant
import com.raul.quickcart.utils.euros

class CartFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater)
        setupRecyclerView()
        setupListeners()
        setupObservers()
        return binding.root
    }

    // TODO: Configura del recycler.
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter { productCart, action ->
            when (action) {
                Action.INCREASE -> viewModel.increaseProductQuantity(productCart)
                Action.DECREASE -> viewModel.decreaseProductQuantity(productCart)
            }
        }
        binding.cartView.adapter = cartAdapter
        binding.cartView.layoutManager = LinearLayoutManager(context)
    }

    // TODO: Configura los observadores para los datos en el ViewModel.
    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.setItems(items)
            updateOrderSummary(items)
            updateOrderButtonState()
        }

        viewModel.updatedProduct.observe(viewLifecycleOwner) { productCart ->
            cartAdapter.updateItem(productCart)
        }

        userViewModel.defaultAddress.observe(viewLifecycleOwner) { address ->
            if (address != null) {
                binding.txtDireccion.text = address.address
            } else {
                binding.txtDireccion.text = getString(R.string.no_default_address)
            }
            updateOrderButtonState()
        }

        userViewModel.defaultCreditCard.observe(viewLifecycleOwner) { creditCard ->
            if (creditCard != null) {
                binding.txtCreditCard.text = "**** **** **** ${creditCard.number.takeLast(4)}"
            } else {
                binding.txtCreditCard.text = getString(R.string.no_default_credit_card)
            }
            updateOrderButtonState()
        }
    }

    // TODO: Método para actualizar el boton de pedido.
    private fun updateOrderButtonState() {
        val itemsNotEmpty = viewModel.cartItems.value?.isNotEmpty() == true
        val addressAvailable = userViewModel.defaultAddress.value != null
        val creditCardAvailable = userViewModel.defaultCreditCard.value != null
        val userLoggedIn = Constant.currentUser != null

        if (userLoggedIn) {
            binding.orderButton.isEnabled = itemsNotEmpty && addressAvailable && creditCardAvailable
            if (!addressAvailable && !creditCardAvailable) {
                binding.errorMessage.text = getString(R.string.warning_no_address_no_credit_card)
            } else if (!addressAvailable) {
                binding.errorMessage.text = getString(R.string.warning_no_address)
            } else if (!creditCardAvailable) {
                binding.errorMessage.text = getString(R.string.warning_no_credit_card)
            } else {
                binding.errorMessage.text = ""
            }
        } else {
            binding.orderButton.isEnabled = itemsNotEmpty
            binding.errorMessage.text = ""
        }

        binding.errorMessage.visibility =
            if (binding.errorMessage.text.isNotEmpty()) View.VISIBLE else View.GONE
    }

    // TODO: Método para actualizar el resumen del pedido.
    private fun updateOrderSummary(items: List<ProductCart>) {
        val subtotal = items.sumOf { it.priceAfterDiscount * it.quantity }
        val delivery = items.sumOf { it.delivery }
        val tax = items.sumOf { (it.pricePerUnit * it.tax) * it.quantity }
        val total = subtotal + delivery + tax

        binding.subtotal.text = subtotal.euros()
        binding.envio.text = delivery.toDouble().euros()
        binding.impuesto.text = tax.euros()
        binding.total.text = total.euros()
    }

    // TODO: Configura los listeners para los elementos de la vista.
    private fun setupListeners() {
        binding.orderButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.orderButton -> {
                if (Constant.currentUser == null) {
                    findNavController().navigate(R.id.got_to_auth_navigation)
                } else {
                    showOrderConfirmationDialog()
                }
            }
        }
    }

    // TODO: Alert para la compra.
    private fun showOrderConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.order_confirmation)
            .setMessage(R.string.order_success_message)
            .setPositiveButton(R.string.title_acept) { _, _ ->
                viewModel.clearCart()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}








