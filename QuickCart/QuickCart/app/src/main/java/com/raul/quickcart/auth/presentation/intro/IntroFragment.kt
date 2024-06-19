package com.raul.quickcart.auth.presentation.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.R
import com.raul.quickcart.databinding.FragmentIntroBinding
import com.raul.quickcart.utils.Utils

class IntroFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(layoutInflater)

        setupListeners()
        return binding.root
    }

    // TODO: Configura los listeners para los elementos de la vista.
    private fun setupListeners() {
        binding.btnIntroLogin.setOnClickListener(this)
        binding.btnIntroRegister.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgClose -> {
                findNavController().popBackStack()
            }

            binding.btnIntroLogin -> {
                findNavController().navigate(
                    R.id.goToLoginFragment
                )
            }

            binding.btnIntroRegister -> {
                findNavController().navigate(
                    R.id.goToRegisterFragment
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibilityBottomNav(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        Utils.hideKeyboard(requireActivity())
        _binding = null
    }
}