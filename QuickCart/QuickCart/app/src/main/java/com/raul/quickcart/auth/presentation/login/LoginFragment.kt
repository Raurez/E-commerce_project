package com.raul.quickcart.auth.presentation.login

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
import com.raul.quickcart.auth.presentation.viewModels.LoginViewModel
import com.raul.quickcart.databinding.FragmentLoginBinding
import com.raul.quickcart.utils.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var textFieldEmail: TextInputEditText
    private lateinit var textFieldPassword: TextInputEditText
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        textFieldEmail = binding.textFieldEmail.editText as TextInputEditText
        textFieldPassword = binding.textFieldPassword.editText as TextInputEditText

        setUpObservers()
        setupListeners()
        return binding.root
    }

    // TODO: Configura los observadores para los datos en el ViewModel.
    private fun setUpObservers() {
        lifecycleScope.launch {
            loginViewModel.isValidForm.collectLatest {

                binding.textFieldEmail.editText?.error = null
                binding.textFieldPassword.editText?.error = null
                binding.textFieldPassword.isEndIconVisible = true

                it.forEach { resourceState ->
                    when (resourceState) {
                        is ResourceState.Success -> Unit

                        is ResourceState.Error -> {
                            when (resourceState.type) {
                                ErrorTypeValidLogin.EMAIL -> {
                                    binding.textFieldEmail.editText?.error = resourceState.msg
                                }

                                ErrorTypeValidLogin.PASSWORD -> {
                                    binding.textFieldPassword.isEndIconVisible = false
                                    binding.textFieldPassword.error = resourceState.msg
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
            loginViewModel.loginResp.collectLatest {
                binding.buttonEnterLogin.isEnabled = it !is ResourceState.Loading
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

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        Utils.hideKeyboard(requireActivity())
        _binding = null
    }

    // TODO: Configura los listeners para los elementos de la vista.
    private fun setupListeners() {
        binding.txtForget.setOnClickListener(this)
        binding.buttonEnterLogin.setOnClickListener(this)
        binding.buttonGoogle.setOnClickListener(this)
        binding.imgBackArrow.setOnClickListener(this)
    }

    // TODO: Navega a la pantalla de inicio.
    private fun showHome() {
        findNavController().navigate(R.id.got_to_home)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibilityBottomNav(false)
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.buttonEnterLogin -> {
                loginViewModel.login(
                    binding.textFieldEmail.editText?.text.toString(),
                    binding.textFieldPassword.editText?.text.toString(),
                    layoutInflater.context
                )
            }

            binding.buttonGoogle -> {
                Utils.showAlert(
                    requireContext(),
                    getString(R.string.title_notice),
                    getString(R.string.function_not_implemented)
                )
            }

            binding.txtForget -> {
                findNavController().navigate(
                    R.id.goToRecoveryFragment
                )
            }

            binding.imgBackArrow -> {
                findNavController().popBackStack()
            }
        }
    }
}