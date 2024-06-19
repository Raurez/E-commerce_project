package com.raul.quickcart.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.google.firebase.auth.FirebaseAuth
import com.raul.quickcart.R
import com.raul.quickcart.auth.data.models.ResourceState
import com.raul.quickcart.databinding.FragmentProfileBinding
import com.raul.quickcart.profile.presentation.viewModels.ProfileViewModel
import com.raul.quickcart.utils.SettingsDataStore
import com.raul.quickcart.utils.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Fragmento para manejar la vista de perfil del usuario.
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    // TODO: Configuración inicial de la vista.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        setupView()
        setupListeners()
        setupObservers()
        setupDarkMode()
        return binding.root
    }

    // TODO: Configura el estado inicial del modo oscuro.
    private fun setupDarkMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            SettingsDataStore.shared.getBooleanValue("dark mode").collectLatest {
                binding.themeModeSelector.isChecked = it
                viewModel.changeStateDarkModeSelector(
                    binding.themeModeSelector.isChecked,
                    binding.icThemeMode,
                    binding.themeModeSelector
                )
            }
        }
    }

    // TODO: Carga los datos del usuario.
    private fun setupView() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModel.loadUserData(userId)
    }

    // TODO: Observa y maneja los cambios en el modo oscuro y los datos del usuario.
    private fun setupObservers() {
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            with(binding) {
                name.text = user.name
                username.text = user.email
                icImage.load(user.photoUrl) {
                    placeholder(R.drawable.ic_image)
                    error(R.drawable.ic_image)
                    crossfade(false)
                    scale(Scale.FILL)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.logoutResult.collectLatest { res ->
                when (res) {
                    is ResourceState.Loading -> {
                        // Todo: Show loading spinner or some other indication
                    }

                    is ResourceState.Success -> {
                        findNavController().navigate(R.id.got_to_auth_navigation)
                    }

                    is ResourceState.Error -> {
                        Utils.showAlert(requireContext(), getString(R.string.title_Error), res.msg)
                    }
                }
            }
        }
    }

    // TODO: Configura los listeners de clic para los elementos de la vista.
    private fun setupListeners() {
        binding.btnCloseSession.setOnClickListener(this)
        binding.icImage.setOnClickListener(this)
        binding.materialCardView3.setOnClickListener(this)
        binding.materialCardView2.setOnClickListener(this)
        binding.materialCardView4.setOnClickListener(this)
        binding.areaPrivateCard.setOnClickListener(this)
        binding.themeModeSelector.setOnClickListener(this)
        binding.themeModeSelector.setOnClickListener {
            val currentUser = viewModel.userData.value
            currentUser?.let {
                val updatedUser = it.copy(darkMode = binding.themeModeSelector.isChecked)
                viewModel.updateUserData(updatedUser)
            }
            viewModel.applyTheme(
                binding.themeModeSelector.isChecked,
                binding.icThemeMode,
                binding.themeModeSelector
            )
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCloseSession -> {
                viewModel.logOut()
            }

            binding.materialCardView3 -> {
                Utils.showAlert(
                    requireContext(),
                    getString(R.string.title_notice),
                    getString(R.string.function_not_implemented)
                )
            }

            binding.icImage -> {
                Utils.showAlert(
                    requireContext(),
                    getString(R.string.title_notice),
                    getString(R.string.function_not_implemented)
                )
            }

            binding.materialCardView2 -> {
                findNavController().navigate(
                    R.id.goToLanguage
                )
            }

            binding.materialCardView4 -> {
                findNavController().navigate(
                    R.id.goToAboutUs
                )
            }

            binding.areaPrivateCard -> {
                findNavController().navigate(
                    R.id.goToPrivateArea
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

