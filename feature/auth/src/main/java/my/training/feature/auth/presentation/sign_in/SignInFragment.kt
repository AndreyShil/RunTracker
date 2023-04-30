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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import my.training.core.core_api.di.AppWithFacade
import my.training.core.core_api.di.HomeMediator
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
    private val viewModel: SignInViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SignInComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextChangeListeners()


        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    binding.btnSignIn.isEnabled = it.isValid()
                    if (it.error != null) {
                        Snackbar.make(requireView(), it.error.localizedMessage ?: "Unknown", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.doAction(SignInAction.DoLoginRequest)
        }

        binding.tvRegister.setSpannableText(
            binding.tvRegister.getEmptySpannableString()
                .appendData(getSpannableData())
                .build()
        )
    }

    private fun initTextChangeListeners() {
        binding.loginTextField.doAfterTextChanged {
            viewModel.doAction(
                SignInAction.OnLoginChanged(inputLogin = it.toString())
            )
        }

        binding.passwordTextField.doAfterTextChanged {
            viewModel.doAction(
                SignInAction.OnPasswordChanged(inputPassword = it.toString())
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

}