package com.raul.quickcart.detail.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.raul.quickcart.R
import com.raul.quickcart.activity.data.NavOption
import com.raul.quickcart.activity.presentation.MainActivity
import com.raul.quickcart.cart.data.createProductCartFromProduct
import com.raul.quickcart.cart.presentation.viewModels.CartViewModel
import com.raul.quickcart.core.data.Products
import com.raul.quickcart.databinding.FragmentDetailBinding
import com.raul.quickcart.detail.presentation.viewModels.DetailViewModel
import com.raul.quickcart.utils.Utils
import com.raul.quickcart.utils.euros
import com.raul.quickcart.utils.toDecimalFormat

class DetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater)
        val productId =
            arguments?.getString("productId") ?: throw IllegalStateException("No product ID found")
        viewModel.loadProductDetails(productId)
        setupListeners()
        setupObservers()
        return binding.root
    }

    // TODO: Configura los observadores para los datos en el ViewModel.
    private fun setupObservers() {
        viewModel.productDetails.observe(viewLifecycleOwner) { product ->
            updateUI(product)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }
    }

    // TODO: Actualiza la interfaz de usuario con los detalles del producto.
    private fun updateUI(product: Products) {
        val language = Utils.getDeviceLanguage()
        val productName = if (language == "en") product.name_en else product.name
        val productDescription =
            if (language == "en") product.description_en else product.description

        with(binding) {
            textView11.text = productName
            descript.text = productDescription
            priceTxt.text = product.totalPrice.euros()
            textView15.text = product.valoracion.toDecimalFormat()

            itemPic.load(product.image) {
                crossfade(true)
                placeholder(R.drawable.logoredondo)
                error(R.drawable.logo2)
            }

            discountIcon.isVisible = product.discount > 0
            if (product.discount > 0) {
                discountIcon.text = getString(R.string.discount_20_off)
            }
        }
    }

    // TODO: Actualiza el ícono de favoritos según el estado del producto.
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val favoriteIcon =
            if (isFavorite) R.drawable.heart_solid else R.drawable.profile_heart_emptyr
        binding.favoritos.setImageResource(favoriteIcon)
    }

    // TODO: Configura los listeners para los elementos de la interfaz de usuario.
    private fun setupListeners() {
        binding.backBtn.setOnClickListener(this)
        binding.btnCompra.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
        binding.favoritos.setOnClickListener(this)
        binding.imageView13.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibilityBottomNav(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).visibilityBottomNav(true)
        _binding = null
    }

    // TODO: Navega hacia atrás en la pila de fragmentos al hacer clic en el botón de retroceso.
    private fun navigateToCart() {
        findNavController().popBackStack()
        (requireActivity() as MainActivity).navigateOptionNavMenu(NavOption.CART)
    }

    // TODO: Comparte los detalles del producto.
    private fun shareProduct(product: Products) {
        val language = Utils.getDeviceLanguage()
        val productName = if (language == "en") product.name_en else product.name
        val productDescription =
            if (language == "en") product.description_en else product.description
        val appName = getString(R.string.app_name)

        val shareText = """
            Check out this amazing product on $appName:
            
            Name: $productName
            Description: $productDescription
            Price: ${product.totalPrice.euros()}
            
            Download our app for more amazing products!
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_product)))
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                findNavController().popBackStack()
            }

            binding.btnCompra -> {
                viewModel.productDetails.value?.let { product ->
                    val productCart = createProductCartFromProduct(product, 1)
                    cartViewModel.addToCart(productCart)
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_alert_cart),
                        getString(R.string.title_add_cart_product)
                    )
                    navigateToCart()
                } ?: run {
                    Toast.makeText(
                        context,
                        "Error: Product ID is not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.favoritos -> {
                viewModel.toggleFavorite()
            }

            binding.btnAddToCart -> {
                viewModel.productDetails.value?.let { product ->
                    val productCart = createProductCartFromProduct(product, 1)
                    cartViewModel.addToCart(productCart)
                    Utils.showAlert(
                        requireContext(),
                        getString(R.string.title_alert_cart),
                        getString(R.string.title_add_cart_product)
                    )
                } ?: run {
                    Toast.makeText(
                        context,
                        "Error: Product ID is not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.imageView13 -> {
                viewModel.productDetails.value?.let { product ->
                    shareProduct(product)
                } ?: run {
                    Toast.makeText(
                        context,
                        "Error: Product details are not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}







