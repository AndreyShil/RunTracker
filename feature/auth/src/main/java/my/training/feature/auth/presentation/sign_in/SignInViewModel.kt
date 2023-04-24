package my.training.feature.auth.presentation.sign_in

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.HomeMediator
import javax.inject.Inject

internal class SignInViewModel(
    private val homeMediator: HomeMediator
) : ViewModel() {

//    fun openMainScreen(fragment: Fragment) {
//        homeMediator.openMainScreen(fragment)
//    }

}

internal class SignInViewModelFactory @Inject constructor(
    private val homeMediator: HomeMediator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(homeMediator) as T
    }
}