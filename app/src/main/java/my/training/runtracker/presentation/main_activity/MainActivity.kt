package my.training.runtracker.presentation.main_activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch
import my.training.runtracker.App
import my.training.runtracker.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainActivityComponent
            .create((application as App).getFacade())
            .inject(this)

        initEffectObserver()
    }

    private fun initEffectObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect {
                    when (it) {
                        MainActivityEffect.OpenAuthGraph -> {
                            setStartDestination(my.training.feature.auth.R.id.auth_graph)
                        }

                        MainActivityEffect.OpenMainGraph -> {
                            setStartDestination(R.id.mainFragment)
                        }

                        is MainActivityEffect.ShowError -> {
                            Toast.makeText(
                                this@MainActivity,
                                it.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
        }
    }

    private fun setStartDestination(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.main_nav_graph)
        navGraph.setStartDestination(destinationId)
        navController.setGraph(navGraph, bundleOf())
    }

}