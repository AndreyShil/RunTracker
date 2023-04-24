package my.training.feature.auth.presentation.sign_in

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.core_api.AppWithFacade
import my.training.core.core_api.HomeMediator
import my.training.core.ui.extensions.getAttrColor
import my.training.core.ui.spannable_text_view.SpannableData
import my.training.feature.auth.R
import my.training.feature.auth.databinding.FragmentSignInBinding
import my.training.feature.auth.di.SignInComponent
import javax.inject.Inject


internal class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    @Inject
    lateinit var viewModelFactory: SignInViewModelFactory

    @Inject
    lateinit var homeMediator: HomeMediator

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel: SignInViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SignInComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            homeMediator.openMainScreen(this)
        }

        binding.tvRegister.setSpannableText(
            binding.tvRegister.getEmptySpannableString()
                .appendData(getSpannableData())
                .build()
        )
    }

    private fun getSpannableData(): List<SpannableData> {
        return listOf(
            SpannableData(
                text = getString(my.training.core.strings.R.string.have_you_not_account)
            ),
            SpannableData(
                text = " "
            ),
            SpannableData(
                text = getString(my.training.core.strings.R.string.register),
                textColorNormal = getAttrColor(android.R.attr.colorPrimary),
                onClick = ::openSignUpScreen
            )
        )
    }

    private fun openSignUpScreen() {
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

}