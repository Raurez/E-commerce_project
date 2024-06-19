package com.raul.quickcart.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.raul.quickcart.R
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.cart.presentation.viewModels.UserViewModel
import com.raul.quickcart.databinding.PrivateAreaFragmentBinding
import com.raul.quickcart.profile.data.Address
import com.raul.quickcart.profile.data.CreditCard
import com.raul.quickcart.profile.presentation.adapterCard.AddressAdapter
import com.raul.quickcart.profile.presentation.adapterCard.CreditCardAdapter
import com.raul.quickcart.profile.presentation.viewModels.PrivateAreaViewModel
import com.raul.quickcart.utils.Utils

// TODO: Fragmento para el área privada del usuario.
class PrivateAreaFragment : Fragment(), View.OnClickListener {

    private var _binding: PrivateAreaFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrivateAreaViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var addressAdapter: AddressAdapter
    private lateinit var creditCardAdapter: CreditCardAdapter

    // TODO: Método para crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).visibilityBottomNav(false)
        _binding = PrivateAreaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // TODO: Método llamado cuando la vista del fragmento ha sido creado.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserAuthentication()
        setupAdapters()
        setupObservers()
        setupListeners()
    }

    // TODO: Método para verificar la autenticación del usuario.
    private fun checkUserAuthentication() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Utils.showAlert(
                requireContext(),
                getString(R.string.title_Error),
                getString(R.string.title_delete)
            )
            findNavController().popBackStack()
        } else {
            viewModel.loadUser()
        }
    }

    // TODO: Método para configurar los adaptadores de lista.
    private fun setupAdapters() {
        // TODO: Configura el adaptador de direcciones.
        addressAdapter = AddressAdapter(
            setDefaultAction = { address ->
                viewModel.setDefaultAddress(address, onSuccess = {
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_success_add),
                        getString(R.string.address_set_default_success)
                    )
                    viewModel.loadUser()
                    userViewModel.reloadUserData()
                }, onError = {
                    Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                })
            },
            editAction = { address -> showEditAddressDialog(address) },
            deleteAction = { address ->
                showDeleteConfirmDialog(address, onSuccess = {
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_success_add),
                        getString(R.string.address_deleted_success)
                    )
                    viewModel.loadUser()
                    userViewModel.reloadUserData()
                }, onError = {
                    Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                })
            }
        )
        // TODO: Configura el adaptador de tarjetas de crédito.
        creditCardAdapter = CreditCardAdapter(
            setDefaultAction = { creditCard ->
                viewModel.setDefaultCreditCard(creditCard, onSuccess = {
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_success_add),
                        getString(R.string.credit_card_set_default_success)
                    )
                    viewModel.loadUser()
                    userViewModel.reloadUserData()
                }, onError = {
                    Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                })
            },
            editAction = { creditCard -> showEditCreditCardDialog(creditCard) },
            deleteAction = { creditCard ->
                showDeleteConfirmDialog(creditCard, onSuccess = {
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_success_add),
                        getString(R.string.credit_card_deleted_success)
                    )
                    viewModel.loadUser()
                    userViewModel.reloadUserData()
                }, onError = {
                    Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                })
            }
        )
        // TODO: Establece el adaptador de direcciones como adaptador inicial.
        binding.recyclerViewDates.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    // TODO: Método para configurar los observadores de datos.
    private fun setupObservers() {
        viewModel.addressList.observe(viewLifecycleOwner) { addresses ->
            addressAdapter.submitList(addresses)
            toggleEmptyListMessage()
        }

        viewModel.creditCardList.observe(viewLifecycleOwner) { creditCards ->
            creditCardAdapter.submitList(creditCards)
            toggleEmptyListMessage()
        }
    }

    // TODO: Método para alternar el mensaje de lista vacía según el tipo de formulario.
    private fun toggleEmptyListMessage() {
        if (binding.selectorCambio.isChecked) {
            val isCreditCardListEmpty = creditCardAdapter.itemCount == 0
            binding.emptyListMessage.apply {
                text = getString(R.string.title_emptyMessageCreditCard)
                visibility = if (isCreditCardListEmpty) View.VISIBLE else View.GONE
            }
        } else {
            val isAddressListEmpty = addressAdapter.itemCount == 0
            binding.emptyListMessage.apply {
                text = getString(R.string.title_emptyMessageAddress)
                visibility = if (isAddressListEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    // TODO: Método para configurar los listeners de elementos de la interfaz.
    private fun setupListeners() {
        binding.imageView8.setOnClickListener(this)
        binding.selectorCambio.setOnCheckedChangeListener { _, isChecked ->
            toggleForm(isChecked)
        }

        binding.buttonSaveAddress.setOnClickListener {
            val address = binding.textFieldAddress.editText?.text.toString()
            if (validateAddress(address)) {
                viewModel.saveAddress(Address(id = generateId(), address = address), onSuccess = {
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_success_add),
                        getString(R.string.address_added_success)
                    )
                    viewModel.loadUser()
                    userViewModel.reloadUserData()
                }, onError = {
                    Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                })
                binding.textFieldAddress.editText?.text?.clear()
            }
        }

        binding.buttonSaveCreditCard.setOnClickListener {
            val number = binding.edtNumberCreditCard.text.toString()
            val expiry = formatExpiry(binding.edtMothYear.text.toString())
            val cvv = binding.edtCVV.text.toString()
            if (validateCreditCard(number, expiry, cvv)) {
                viewModel.saveCreditCard(
                    CreditCard(
                        id = generateId(),
                        number = number,
                        expiry = expiry,
                        cvv = cvv
                    ), onSuccess = {
                        Utils.showAlert(
                            requireContext(),
                            getString(R.string.title_success_add),
                            getString(R.string.credit_card_added_success)
                        )
                        viewModel.loadUser()
                        userViewModel.reloadUserData()
                    }, onError = {
                        Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                    })
                binding.edtNumberCreditCard.text.clear()
                binding.edtMothYear.text.clear()
                binding.edtCVV.text.clear()
            }
        }
    }

    private fun toggleForm(showCreditCardForm: Boolean) {
        if (showCreditCardForm) {
            binding.addressFormContainer.visibility = View.GONE
            binding.creditCardFormContainer.visibility = View.VISIBLE
            binding.formTitle.setText(R.string.title_Form_card)
            binding.recyclerViewDates.adapter = creditCardAdapter
        } else {
            binding.addressFormContainer.visibility = View.VISIBLE
            binding.creditCardFormContainer.visibility = View.GONE
            binding.formTitle.setText(R.string.title_Form_address)
            binding.recyclerViewDates.adapter = addressAdapter
        }
        toggleEmptyListMessage()
    }

    private fun showEditAddressDialog(address: Address) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_address, null)
        val editTextAddress = dialogView.findViewById<EditText>(R.id.editTextAddress)
        editTextAddress.setText(address.address)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_edit_address)
            .setView(dialogView)
            .setPositiveButton(R.string.title_save) { _, _ ->
                val updatedAddress = address.copy(address = editTextAddress.text.toString())
                if (validateAddress(updatedAddress.address)) {
                    viewModel.updateAddress(updatedAddress, onSuccess = {
                        Utils.showAlert(
                            requireContext(),
                            getString(R.string.title_success_add),
                            getString(R.string.address_updated_success)
                        )
                        viewModel.loadUser()
                        userViewModel.reloadUserData()
                    }, onError = {
                        Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                    })
                }
            }
            .setNegativeButton(R.string.title_cancel, null)
            .show()
    }

    private fun showEditCreditCardDialog(creditCard: CreditCard) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_credit_card, null)
        val editTextNumber = dialogView.findViewById<EditText>(R.id.editTextCreditCardNumber)
        val editTextExpiry = dialogView.findViewById<EditText>(R.id.editTextExpiry)
        val editTextCVV = dialogView.findViewById<EditText>(R.id.editTextCVV)

        editTextNumber.setText(creditCard.number)
        editTextExpiry.setText(creditCard.expiry)
        editTextCVV.setText(creditCard.cvv)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_edit_credit_card)
            .setView(dialogView)
            .setPositiveButton(R.string.title_save) { _, _ ->
                val updatedCreditCard = creditCard.copy(
                    number = editTextNumber.text.toString(),
                    expiry = editTextExpiry.text.toString(),
                    cvv = editTextCVV.text.toString()
                )
                if (validateCreditCard(
                        updatedCreditCard.number,
                        updatedCreditCard.expiry,
                        updatedCreditCard.cvv
                    )
                ) {
                    viewModel.updateCreditCard(updatedCreditCard, onSuccess = {
                        Utils.showAlert(
                            requireContext(),
                            getString(R.string.title_success_add),
                            getString(R.string.credit_card_updated_success)
                        )
                        viewModel.loadUser()
                        userViewModel.reloadUserData()
                    }, onError = {
                        Utils.showAlert(requireContext(), getString(R.string.title_Error), it)
                    })
                } else {
                    showCreditCardValidationErrors(editTextNumber, editTextExpiry, editTextCVV)
                }
            }
            .setNegativeButton(R.string.title_cancel, null)
            .show()
    }

    private fun showDeleteConfirmDialog(
        item: Any,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_confirm_delete)
            .setMessage(R.string.msg_confirm_delete)
            .setPositiveButton(R.string.title_delete) { dialog, _ ->
                when (item) {
                    is Address -> viewModel.deleteAddress(
                        item,
                        onSuccess = {
                            onSuccess()
                            viewModel.loadUser()
                            userViewModel.reloadUserData()
                        },
                        onError = onError
                    )

                    is CreditCard -> viewModel.deleteCreditCard(
                        item,
                        onSuccess = {
                            onSuccess()
                            viewModel.loadUser()
                            userViewModel.reloadUserData()
                        },
                        onError = onError
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.title_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // TODO: Método para manejar los clics en los elementos de la interfaz.
    override fun onClick(v: View?) {
        when (v) {
            binding.imageView8 -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun generateId(): String {
        return System.currentTimeMillis().toString()
    }

    private fun validateCreditCard(number: String, expiry: String, cvv: String): Boolean {
        val expiryRegex = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
        val isValid = number.length == 16 && cvv.length == 3 && expiry.matches(expiryRegex)

        if (!isValid) {
            if (number.length != 16) binding.edtNumberCreditCard.error =
                getString(R.string.invalid_card_number)
            if (cvv.length != 3) binding.edtCVV.error = getString(R.string.invalid_cvv)
            if (!expiry.matches(expiryRegex)) binding.edtMothYear.error =
                getString(R.string.invalid_expiry_date)
        }

        return isValid
    }

    private fun validateAddress(address: String): Boolean {
        return if (address.isNotEmpty()) {
            true
        } else {
            binding.textFieldAddress.error = getString(R.string.invalid_credit_card_details)
            false
        }
    }

    private fun formatExpiry(expiry: String): String {
        return if (expiry.length == 4 && !expiry.contains("/")) {
            expiry.substring(0, 2) + "/" + expiry.substring(2, 4)
        } else {
            expiry
        }
    }

    private fun showCreditCardValidationErrors(
        numberField: EditText,
        expiryField: EditText,
        cvvField: EditText
    ) {
        if (numberField.text.length != 16) numberField.error =
            getString(R.string.invalid_card_number)
        if (cvvField.text.length != 3) cvvField.error = getString(R.string.invalid_cvv)
        if (!expiryField.text.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))) expiryField.error =
            getString(R.string.invalid_expiry_date)
    }

    // TODO: Método llamado cuando el fragmento está siendo destruido.
    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        _binding = null
        Utils.hideKeyboard(requireActivity())
    }
}
























