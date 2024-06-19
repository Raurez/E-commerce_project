package com.raul.quickcart.home.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raul.quickcart.R
import com.raul.quickcart.databinding.FragmentHomeBinding
import com.raul.quickcart.home.presentation.adapters.CarruselAdapter
import com.raul.quickcart.home.presentation.viewModels.HomeViewModel
import com.raul.quickcart.profile.presentation.adapterSearch.SearchResultsAdapter
import com.raul.quickcart.utils.Constant
import com.raul.quickcart.utils.Utils

class HomeFragment : Fragment(), View.OnClickListener {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var carruselAdapter: CarruselAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearchResultsRecyclerView()
        setupListeners()
        setUpObservers()

        homeViewModel.loadProducts()

        return root
    }

    override fun onResume() {
        super.onResume()
        setupWelcomeMessage()
    }

    // TODO: Configura el mensaje de bienvenida.
    private fun setupWelcomeMessage() {
        val user = Constant.currentUser

        if (user != null) {
            binding.toolbar.logoImageView.visibility = View.GONE
            binding.toolbar.textViewWelcome.visibility = View.VISIBLE
            binding.toolbar.textView4.text = user.displayName
            binding.toolbar.textViewWelcome.text = getString(R.string.title_Welcome_home)
            binding.toolbar.textViewWelcome.isClickable = false
        } else {
            binding.toolbar.logoImageView.visibility = View.VISIBLE
            binding.toolbar.textViewWelcome.text = getString(R.string.title_login)
            binding.toolbar.textViewWelcome.visibility = View.VISIBLE
            binding.toolbar.textView4.visibility = View.GONE
            binding.toolbar.textViewWelcome.isClickable = true
            binding.toolbar.textViewWelcome.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    // TODO: Navega a la pantalla de login.
    private fun navigateToLogin() {
        findNavController().navigate(R.id.auth_navigation)
    }

    // TODO: Configura el RecyclerView del carrusel de productos.
    private fun setupRecyclerView() {
        carruselAdapter = CarruselAdapter(arrayListOf(), requireContext()) { product ->
            val bundle = bundleOf("productId" to product.id)
            findNavController().navigate(R.id.goToDetail, bundle)
        }
        binding.PopularViewCarrusel.adapter = carruselAdapter
        binding.PopularViewCarrusel.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    // TODO: Configura el RecyclerView de los resultados de búsqueda.
    private fun setupSearchResultsRecyclerView() {
        searchResultsAdapter = SearchResultsAdapter { product ->
            val bundle = bundleOf("productId" to product.id)
            findNavController().navigate(R.id.goToDetail, bundle)
        }
        binding.toolbar.recyclerViewSearchResults.adapter = searchResultsAdapter
        binding.toolbar.recyclerViewSearchResults.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    // TODO: Configura los listeners para los clics en los botones y la barra de búsqueda.
    private fun setupListeners() {
        binding.seeAll.setOnClickListener(this)
        binding.btnBuyNow.setOnClickListener(this)
        binding.imgElectronic.setOnClickListener(this)
        binding.imgClothes.setOnClickListener(this)
        binding.imgHome.setOnClickListener(this)
        binding.imgBeauty.setOnClickListener(this)
        binding.imgSeeAll.setOnClickListener(this)
        binding.toolbar.imageView2.setOnClickListener(this)

        binding.toolbar.editTextText4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    homeViewModel.searchProducts(query)
                    binding.toolbar.recyclerViewSearchResults.visibility = View.VISIBLE
                } else {
                    searchResultsAdapter.setProducts(emptyList())
                    binding.toolbar.recyclerViewSearchResults.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // TODO: Configura los observadores para los LiveData.
    private fun setUpObservers() {
        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            carruselAdapter.updateList(products)
        }
        homeViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchResultsAdapter.setProducts(results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Utils.hideKeyboard(requireActivity())
    }

    // TODO: Maneja los clics en los botones de la interfaz.
    override fun onClick(v: View?) {
        when (v) {
            binding.toolbar.imageView2 -> {
                Utils.showAlert(
                    requireContext(),
                    getString(R.string.title_notice),
                    getString(R.string.function_not_implemented)
                )
            }

            binding.seeAll -> navigateToCategory(getString(R.string.title_Popular_product))
            binding.btnBuyNow -> navigateToCategory(getString(R.string.title_Electronics))
            binding.imgElectronic -> navigateToCategory(getString(R.string.title_Electronics))
            binding.imgClothes -> navigateToCategory(getString(R.string.title_Clothes))
            binding.imgHome -> navigateToCategory(getString(R.string.title_Home_hogar))
            binding.imgBeauty -> navigateToCategory(getString(R.string.title_Beauty))
            binding.imgSeeAll -> navigateToCategory(getString(R.string.title_See_all))
        }
    }

    // TODO: Navega a la pantalla de categoría específica con el nombre de la categoría.
    private fun navigateToCategory(category: String) {
        val bundle = bundleOf("category" to category, "title" to category)
        findNavController().navigate(R.id.goToProducts, bundle)
    }
}



