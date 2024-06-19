package com.raul.quickcart.auth.presentation.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.raul.quickcart.R
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.auth.data.models.ErrorTypeValidLogin
import com.raul.quickcart.auth.data.models.ResourceState
import com.raul.quickcart.auth.presentation.viewModels.RegisterViewModel
import com.raul.quickcart.databinding.FragmentRegisterBinding
import com.raul.quickcart.utils.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var textFieldName: TextInputEditText
    private lateinit var textFieldEmail: TextInputEditText
    private lateinit var textFieldPassword: TextInputEditText
    private lateinit var textFieldPasswordRepeat: TextInputEditText
    private lateinit var textFieldPhone: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        textFieldName = binding.textFieldName.editText as TextInputEditText
        textFieldEmail = binding.textFieldEmail.editText as TextInputEditText
        textFieldPassword = binding.textFieldPassword.editText as TextInputEditText
        textFieldPasswordRepeat = binding.textFieldPasswordRepeat.editText as TextInputEditText
        textFieldPhone = binding.textFieldPhone.editText as TextInputEditText

        setupListeners()
        setUpObservers()
        return binding.root
    }

    // TODO: Configura los observadores para los datos en el ViewModel.
    private fun setUpObservers() {
        lifecycleScope.launch {
            registerViewModel.isValidForm.collectLatest {

                binding.textFieldEmail.error = null
                binding.textFieldName.error = null
                binding.textFieldPassword.error = null
                binding.textFieldPassword.isEndIconVisible = true
                binding.textFieldPasswordRepeat.error = null
                binding.textFieldPasswordRepeat.isEndIconVisible = true
                binding.textFieldPhone.error = null

                it.forEach { resourceState ->
                    when (resourceState) {
                        is ResourceState.Success -> Unit

                        is ResourceState.Error -> {
                            when (resourceState.type) {
                                ErrorTypeValidLogin.NAME -> {
                                    binding.textFieldName.error = resourceState.msg
                                }

                                ErrorTypeValidLogin.EMAIL -> {
                                    binding.textFieldEmail.error = resourceState.msg
                                }

                                ErrorTypeValidLogin.PASSWORD -> {
                                    binding.textFieldPassword.isEndIconVisible = false
                                    binding.textFieldPassword.error = resourceState.msg
                                }

                                ErrorTypeValidLogin.REPEAT_PASSWORD -> {
                                    binding.textFieldPasswordRepeat.isEndIconVisible = false
                                    binding.textFieldPasswordRepeat.error = resourceState.msg
                                }

                                ErrorTypeValidLogin.PHONE -> {
                                    binding.textFieldPhone.error = resourceState.msg
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
            registerViewModel.registerResp.collectLatest {
                binding.buttonEnterRegister.isEnabled = it !is ResourceState.Loading
                when (it) {

                    is ResourceState.Success -> {
                        showHome()
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

    // TODO: Navega a la pantalla de inicio.
    private fun showHome() {
        binding.buttonEnterRegister.findNavController().navigate(R.id.got_to_home)
    }

    // TODO: Configura los listeners para los elementos de la vista.
    private fun setupListeners() {
        binding.arrowBack.setOnClickListener(this)
        binding.buttonEnterRegister.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        Utils.hideKeyboard(requireActivity())
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibilityBottomNav(false)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.arrowBack -> {
                findNavController().popBackStack()
            }

            binding.buttonEnterRegister -> {
                registerViewModel.register(
                    binding.textFieldName.editText?.text.toString(),
                    binding.textFieldEmail.editText?.text.toString(),
                    binding.textFieldPassword.editText?.text.toString(),
                    binding.textFieldPasswordRepeat.editText?.text.toString(),
                    binding.textFieldPhone.editText?.text.toString(),
                    layoutInflater.context
                )
            }
        }
    }
}

