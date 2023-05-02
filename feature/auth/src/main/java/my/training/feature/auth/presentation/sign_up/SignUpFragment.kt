package my.training.feature.auth.presentation.sign_up

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import my.training.core.core_api.di.AppWithFacade
import my.training.core.core_api.di.HomeMediator
import my.training.core.ui.extensions.hideKeyboard
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.auth.R
import my.training.feature.auth.databinding.FragmentSignUpBinding
import my.training.feature.auth.di.SignUpComponent
import javax.inject.Inject

internal class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var viewModelFactory: SignUpViewModelFactory

    @Inject
    lateinit var homeMediator: HomeMediator

    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: SignUpViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SignUpComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUiStateObserver()
        initUiEffectObserver()
        initTextChangeListeners()

        binding.btnSignUp.setOnClickListener {
            hideKeyboard()
            viewModel.setEvent(SignUpContract.Event.OnRegisterClicked)
        }
    }

    private fun initUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    binding.btnSignUp.buttonEnabled = it.isValid()
                    binding.btnSignUp.updateLoadingState(it.isLoading)
                    updatePasswordError(it.passwordError)
                    updateEmailError(it.emailError)
                }
            }
        }
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is SignUpContract.Effect.ShowError -> {
                            showSnackbar(effect.errorMessage)
                        }

                        SignUpContract.Effect.OpenMainScreen -> {
                            openMainScreen()
                        }
                    }
                }
            }
        }
    }

    private fun initTextChangeListeners() {
        binding.run {
            loginTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnLoginChanged(inputLogin = it.toString())
                )
            }

            firstNameTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnFirstNameChanged(inputFirstName = it.toString())
                )
            }

            lastNameTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnLastNameChanged(inputLastName = it.toString())
                )
            }

            emailTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnEmailChanged(inputEmail = it.toString())
                )
            }

            passwordTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnPasswordChanged(inputPassword = it.toString())
                )
            }

            repeatPasswordTextField.doAfterTextChanged {
                viewModel.setEvent(
                    SignUpContract.Event.OnPasswordRepeatChanged(inputPasswordRepeat = it.toString())
                )
            }

        }
    }

    private fun updatePasswordError(hasError: Boolean) {
        val errorString = if (hasError)
            getString(my.training.core.strings.R.string.passwords_not_matched)
        else
            null

        binding.passwordInputLayout.error = errorString
        binding.repeatPasswordInputLayout.error = errorString
    }

    private fun updateEmailError(hasError: Boolean) {
        val errorString = if (hasError)
            getString(my.training.core.strings.R.string.email_invalid)
        else
            null
        binding.emailInputLayout.error = errorString
    }

    private fun openMainScreen() {
        homeMediator.openMainScreen(this)
    }

}