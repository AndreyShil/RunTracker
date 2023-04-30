package my.training.runtracker.di

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.training.core.core_api.di.HomeMediator
import my.training.runtracker.MainNavGraphDirections
import javax.inject.Inject

class HomeMediatorImpl @Inject constructor() : HomeMediator {

    override fun openMainScreen(fragment: Fragment) {
        val action = MainNavGraphDirections.actionGlobalMainFragment()
        fragment.findNavController().navigate(action)
    }
}