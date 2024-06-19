package com.raul.quickcart.activity.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raul.quickcart.R
import com.raul.quickcart.activity.data.NavOption
import com.raul.quickcart.activity.presentation.viewModel.ActivityViewModel
import com.raul.quickcart.databinding.ActivityMainBinding
import com.raul.quickcart.utils.Constant
import com.raul.quickcart.utils.SettingsDataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Instala la pantalla de presentación (splashScreen).
        val splashScreen = installSplashScreen()
        // TODO: Mantiene la pantalla de presentación visible.
        splashScreen.setKeepOnScreenCondition { true }
        // TODO: Verifica la autenticación del usuario y oculta la pantalla de presentación una vez completada.
        viewModel.checkAuthentication {
            splashScreen.setKeepOnScreenCondition { false }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        setupListeners()
        setupDarkMode()
        setupLanguage()
    }

    // TODO: Configura el modo oscuro según la preferencia almacenada.
    private fun setupDarkMode() {
        lifecycleScope.launch {
            SettingsDataStore.shared.getBooleanValue("dark mode").collectLatest {
                val mode = if (it)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    // TODO: Configura el idioma según la preferencia almacenada.
    private fun setupLanguage() {
        lifecycleScope.launch {
            SettingsDataStore.shared.getStringValue("selected_language").collectLatest { language ->
                if (language.isNotEmpty()) {
                    val locale = Locale(language)
                    val res = resources
                    val configuration = res.configuration

                    val localeList = LocaleList(locale)
                    LocaleList.setDefault(localeList)
                    configuration.setLocales(localeList)

                    createConfigurationContext(configuration)
                    resources.updateConfiguration(configuration, res.displayMetrics)
                }
            }
        }
    }

    // TODO: Navega a la opción de menú de navegación especificada.
    fun navigateOptionNavMenu(option: NavOption) {
        binding.navView.selectedItemId = option.rawValue
    }

    // TODO: Configura los listeners para los cambios de destino del NavController.
    private fun setupListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.isVisible = when (destination.id) {
                R.id.loginFragment, R.id.registerFragment, R.id.forgotPasswordFragment -> false
                else -> true
            }

            if (destination.id == R.id.navigation_profile && Constant.currentUser == null) {
                navigateOptionNavMenu(NavOption.HOME)
                navController.navigate(R.id.got_to_auth_navigation)
            }
        }
    }

    // TODO: Adjunta el contexto base con la configuración de idioma predeterminada.
    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration()
        config.setToDefaults()
        config.setLocale(Locale.getDefault())
        super.attachBaseContext(newBase?.createConfigurationContext(config))
    }

    // TODO: Controla la visibilidad de la vista de navegación inferior.
    fun visibilityBottomNav(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }
}