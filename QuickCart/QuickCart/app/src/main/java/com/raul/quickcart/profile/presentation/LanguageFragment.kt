package com.raul.quickcart.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.databinding.FragmentLanguageBinding
import com.raul.quickcart.profile.presentation.adapter.LanguageAdapter
import com.raul.quickcart.profile.presentation.viewModels.LanguageViewModel
import com.raul.quickcart.utils.Data

// TODO: Fragmento para la selección de idioma.
class LanguageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    private val languageViewModel: LanguageViewModel by viewModels()
    private lateinit var languageAdapter: LanguageAdapter

    // TODO: Método para crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).visibilityBottomNav(false)

        setupListeners()
        setupRecyclerView()
        languageViewModel.loadSelectedLanguage(requireContext())
        return binding.root
    }

    // TODO: Método para configurar el RecyclerView que muestra la lista de idiomas.
    private fun setupRecyclerView() {
        languageAdapter = LanguageAdapter(this, languageViewModel.selectedLanguage) {
            languageViewModel.selectLanguage(requireContext(), it)
            requireActivity().recreate()
        }
        binding.RCIdiomas.adapter = languageAdapter
        binding.RCIdiomas.layoutManager = LinearLayoutManager(context)

        if (Data.listLanguage.isNotEmpty()) {
            languageAdapter.setUpList(Data.listLanguage)
        }
    }

    // TODO: Método para configurar los listeners de los elementos de la interfaz.
    private fun setupListeners() {
        binding.imageViewBack.setOnClickListener(this)
    }

    // TODO: Método para manejar los clics en los elementos de la interfaz.
    override fun onClick(v: View?) {
        when (v) {
            binding.imageViewBack -> {
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