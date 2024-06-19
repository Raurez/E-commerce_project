package com.raul.quickcart.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.databinding.FragmentAboutUsBinding

// TODO: Fragmento para la pantalla "Acerca de nosotros".
class AboutUsFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    // TODO: Método para crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).visibilityBottomNav(false)
        setupListeners()
        return binding.root
    }

    // TODO: Método para configurar los listeners de los elementos de la interfaz.
    private fun setupListeners() {
        binding.btnReturn.setOnClickListener(this)
        binding.imageView6.setOnClickListener(this)
    }

    // TODO: Método para manejar los clics en los elementos de la interfaz.
    override fun onClick(v: View?) {
        when (v) {
            binding.imageView6 -> {
                findNavController().popBackStack()
            }

            binding.btnReturn -> {
                findNavController().popBackStack()
            }
        }
    }

    // TODO: Método llamado cuando el fragmento está siendo destruido.
    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        _binding = null
    }
}