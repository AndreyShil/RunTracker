package my.training.runtracker.di

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import my.training.core.core_api.di.HomeMediator
import my.training.runtracker.MainNavGraphDirections
import my.training.runtracker.R
import javax.inject.Inject

class HomeMediatorImpl @Inject constructor() : HomeMediator {

    override fun openMainScreen(fragment: Fragment) {
        val action = MainNavGraphDirections.actionGlobalMainFragment()
        fragment.findNavController().navigate(action)
    }

    override fun openAuthScreen(fragment: Fragment) {
        val navHostFragment = fragment.requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.main_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.main_nav_graph)
        navGraph.setStartDestination(my.training.feature.auth.R.id.auth_graph)
        navController.setGraph(navGraph, bundleOf())
        navController.navigate(my.training.feature.auth.R.id.auth_graph)
    }
}