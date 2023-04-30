package my.training.feature.auth.presentation.sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.core_api.di.AppWithFacade
import my.training.core.core_api.di.HomeMediator
import my.training.feature.auth.R
import my.training.feature.auth.databinding.FragmentSignUpBinding
import my.training.feature.auth.di.SignUpComponent
import javax.inject.Inject

internal class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var homeMediator: HomeMediator

    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SignUpComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            homeMediator.openMainScreen(this)
        }
    }

}