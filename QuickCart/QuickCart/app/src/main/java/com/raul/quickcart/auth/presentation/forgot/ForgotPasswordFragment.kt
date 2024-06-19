package com.raul.quickcart.auth.presentation.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.raul.quickcart.R
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.auth.data.models.ErrorTypeValidLogin
import com.raul.quickcart.auth.data.models.ResourceState
import com.raul.quickcart.auth.presentation.viewModels.ForgotPasswordViewModel
import com.raul.quickcart.databinding.FragmentForgotPasswordBinding
import com.raul.quickcart.utils.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val forgotViewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var textFieldEmail: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        setupListeners()
        setUpObservers()
        textFieldEmail = binding.textFieldEmail.editText as TextInputEditText
        return binding.root
    }

    // TODO: Configura los observadores para los datos en el ViewModel.
    private fun setUpObservers() {
        lifecycleScope.launch {
            forgotViewModel.isValidForm.collectLatest {

                binding.textFieldEmail.error = null

                it.forEach { resourceState ->
                    when (resourceState) {
                        is ResourceState.Success -> Unit

                        is ResourceState.Error -> {
                            when (resourceState.type) {

                                ErrorTypeValidLogin.EMAIL -> {
                                    binding.textFieldEmail.error = resourceState.msg
                                }

                                else -> Unit
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            forgotViewModel.resetPassword.collectLatest {
                binding.buttonEnterForgot.isEnabled = it !is ResourceState.Loading
                when (it) {

                    is ResourceState.Success -> {
                        showLogin()
                    }

                    is ResourceState.Error -> {
                        Utils.showAlert(
                            layoutInflater.context,
                            getString(R.string.title_Error),
                            it.msg
                        )
                    }

                    else -> Unit
                }
            }
        }

    }

    // TODO: Configura los listeners para los elementos de la vista.
    private fun setupListeners() {
        binding.buttonEnterForgot.setOnClickListener(this)
        binding.txtRegister.setOnClickListener(this)
        binding.arrowBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.buttonEnterForgot -> {
                forgotViewModel.resetPassword(
                    binding.textFieldEmail.editText?.text.toString(),
                    layoutInflater.context
                )
            }

            binding.txtRegister -> {
                findNavController().navigate(
                    R.id.action_forgotPasswordFragment_to_registerFragment
                )
            }

            binding.arrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

    // TODO: Muestra la pantalla de inicio de sesión.
    private fun showLogin() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Restaura la visibilidad de la barra de navegación inferior.
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        // TODO: Oculta el teclado virtual.
        Utils.hideKeyboard(requireActivity())
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // TODO: Oculta la barra de navegación inferior mientras está en este fragmento.
        (requireActivity() as MainActivity).visibilityBottomNav(false)
    }
}