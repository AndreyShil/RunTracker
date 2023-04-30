package my.training.feature.auth.presentation.sign_in

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import my.training.core.core_api.di.AppWithFacade
import my.training.core.core_api.di.HomeMediator
import my.training.core.ui.custom_view.spannable_text_view.SpannableData
import my.training.core.ui.extensions.getAttrColor
import my.training.core.ui.extensions.hideKeyboard
import my.training.core.ui.extensions.showSnackbar
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
    private val viewModel: SignInViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SignInComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUiStateObserver()
        initUiEffectObserver()
        initTextChangeListeners()

        binding.btnSignIn.setOnClickListener {
            hideKeyboard()
            viewModel.setEvent(SignInContract.Event.OnLoginClicked)
        }

        binding.tvRegister.setSpannableText(
            binding.tvRegister.getEmptySpannableString()
                .appendData(getSpannableData())
                .build()
        )
    }

    private fun initUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    binding.btnSignIn.buttonEnabled = it.isValid()
                    binding.btnSignIn.updateLoadingState(it.isLoading)
                }
            }
        }
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is SignInContract.Effect.ShowError -> {
                            showSnackbar(effect.errorMessage)
                        }

                        SignInContract.Effect.OpenMainScreen -> {
                            openMainScreen()
                        }
                    }
                }
            }
        }
    }

    private fun initTextChangeListeners() {
        binding.loginTextField.doAfterTextChanged {
            viewModel.setEvent(
                SignInContract.Event.OnLoginChanged(inputLogin = it.toString())
            )
        }

        binding.passwordTextField.doAfterTextChanged {
            viewModel.setEvent(
                SignInContract.Event.OnPasswordChanged(inputPassword = it.toString())
            )
        }
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

    private fun openMainScreen() {
        homeMediator.openMainScreen(this)
    }

}