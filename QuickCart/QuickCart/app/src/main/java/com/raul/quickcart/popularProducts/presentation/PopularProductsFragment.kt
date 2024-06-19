package com.raul.quickcart.popularProducts.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.raul.quickcart.R
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.core.db.ProductsCloud
import com.raul.quickcart.databinding.FragmentPopularProductsBinding
import com.raul.quickcart.popularProducts.presentation.adapter.ProductsAdapter
import com.raul.quickcart.popularProducts.presentation.viewModels.PopularProductsViewModel
import com.raul.quickcart.utils.Utils

// TODO: Fragmento para mostrar productos populares.
class PopularProductsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPopularProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PopularProductsViewModel by viewModels()
    private lateinit var productsAdapter: ProductsAdapter
    private var selectedCategory: String? = null

    // TODO: Crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularProductsBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).visibilityBottomNav(false)

        // TODO: Obtener argumentos pasados al fragmento.
        arguments?.let {
            val title = it.getString("title")
            selectedCategory = it.getString("category")

            if (title?.isNotEmpty() == true)
                binding.title.text = title
        }
        setupRecyclerView()
        setupListeners()
        setupObservers()

        // TODO: Cargar categorías disponibles.
        val productsCloud = ProductsCloud()
        productsCloud.listCategories(
            onSuccess = { categories ->
                Log.d("PopularProductsFragment", "Available categories: $categories")
            },
            onError = { exception ->
                Log.e("PopularProductsFragment", "Error loading categories", exception)
            }
        )

        val popularProductTitle = getString(R.string.title_Popular_product)
        val seeAllTitle = getString(R.string.title_See_all)

        // TODO: Cargar productos según la categoría seleccionada.
        if (selectedCategory != null) {
            Log.d("PopularProductsFragment", "Loading products for category: $selectedCategory")
            viewModel.loadProductsByCategory(selectedCategory!!, popularProductTitle, seeAllTitle)
        } else {
            Log.d("PopularProductsFragment", "Loading all products")
            viewModel.loadProducts()
        }

        return binding.root
    }

    // TODO: Configurar RecyclerView.
    private fun setupRecyclerView() {
        productsAdapter = ProductsAdapter { product ->
            val bundle = bundleOf("productId" to product.id)
            findNavController().navigate(R.id.detailFragment, bundle)
        }
        binding.recyclerViewPopularProducts.adapter = productsAdapter
        binding.recyclerViewPopularProducts.layoutManager = GridLayoutManager(context, 2)
    }

    // TODO: Configurar observadores para LiveData.
    private fun setupObservers() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            Log.d("PopularProductsFragment", "Products received: $products")
            productsAdapter.setProducts(products)
        }
    }

    // TODO: Configurar listeners para eventos de la UI.
    private fun setupListeners() {
        binding.imageView8.setOnClickListener(this)
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipPopular -> viewModel.filterProducts("Popular Product")
                R.id.chipAlfa -> viewModel.filterProducts("Alphabetically")
                R.id.chipBarato -> viewModel.filterProducts("Cheaper Product")
            }
        }

        // TODO: Listener para el buscador.
        binding.buscadorPopular.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchProducts(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // TODO: Acciones al destruir el fragmento.
    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        Utils.hideKeyboard(requireActivity())
        _binding = null
    }

    // TODO: Manejar clics en la UI.
    override fun onClick(v: View?) {
        when (v) {
            binding.imageView8 -> {
                findNavController().popBackStack()
            }
        }
    }
}








