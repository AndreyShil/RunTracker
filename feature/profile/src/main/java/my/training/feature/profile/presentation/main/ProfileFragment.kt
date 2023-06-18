package my.training.feature.profile.presentation.main

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.launch
import my.training.core.core_api.di.HomeMediator
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.core.core_api.domain.model.enums.AppTheme
import my.training.core.core_api.domain.model.enums.FirebaseFolderType
import my.training.core.core_api.domain.model.user.User
import my.training.core.ui.custom_view.item_decoration.AllDividerItemDecoration
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.profile.R
import my.training.feature.profile.databinding.FragmentProfileBinding
import my.training.feature.profile.di.ProfileMainComponent
import my.training.feature.profile.presentation.adapter.SessionsAdapter
import java.util.UUID
import javax.inject.Inject

internal class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var firebaseStorageManager: FirebaseStorageManager

    @Inject
    lateinit var sessionAdapter: SessionsAdapter

    @Inject
    lateinit var homeMediator: HomeMediator

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            uploadImage(it)
        }
    }

    private val appThemeMap = mapOf(
        AppTheme.SYSTEM to R.id.btn_system_theme,
        AppTheme.LIGHT_MODE to R.id.btn_light_theme,
        AppTheme.DARK_MODE to R.id.btn_dark_theme
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProfileMainComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSessionsRecyclerView()

        observeProfileFlow()
        observeUiState()
        initUiEffectObserver()

        binding.avatar.setOnClickListener {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.appThemeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val appTheme = appThemeMap.entries.first { it.value == checkedId }.key
                viewModel.setEvent(
                    ProfileContract.Event.OnAppThemeChanged(appTheme)
                )
            }
        }
        binding.btnLogout.setOnClickListener {
            viewModel.setEvent(
                ProfileContract.Event.OnLogoutClicked
            )
        }
    }

    private fun initSessionsRecyclerView() {
        binding.rvSessions.adapter = sessionAdapter
        binding.rvSessions.addItemDecoration(
            AllDividerItemDecoration(requireContext())
        )
        sessionAdapter.setListener {
            viewModel.setEvent(
                ProfileContract.Event.RemoveSession(
                    sessionId = it
                )
            )
        }
    }

    private fun observeProfileFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileFlow.collect {
                    viewModel.setEvent(
                        ProfileContract.Event.OnUserReceived(
                            user = it
                        )
                    )
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUserInfo(it.user)
                    sessionAdapter.submitList(it.sessions)
                }
            }
        }
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is ProfileContract.Effect.ShowError -> {
                            showSnackbar(effect.errorMessage)
                        }

                        is ProfileContract.Effect.SetInitialThemeSetting -> {
                            setAppThemeCheckedButton(effect.appTheme)
                        }

                        ProfileContract.Effect.OpenAuthScreen -> {
                            homeMediator.openAuthScreen(this@ProfileFragment)
                        }
                    }
                }
            }
        }
    }

    private fun updateUserInfo(user: User) {
        binding.run {
            tvName.text = user.getFullName()
            displayImageFromFirebaseStorage(user.photo)
        }
    }

    private fun setAppThemeCheckedButton(appTheme: AppTheme) {
        val checkedId = appThemeMap[appTheme] ?: R.id.btn_system_theme
        binding.appThemeToggleGroup.check(checkedId)
    }

    private fun displayImageFromFirebaseStorage(imageUrl: String?) {
        imageUrl ?: return
        firebaseStorageManager.downloadImage(imageUrl) {
            binding.avatar.load(
                data = it
            ) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(my.training.core.iconpack.R.drawable.ic_avatar_placeholder)
                error(my.training.core.iconpack.R.drawable.ic_avatar_placeholder)
            }
        }
    }

    private fun uploadImage(fileUri: Uri) {
        firebaseStorageManager.uploadImage(
            fileUri = fileUri,
            folderType = FirebaseFolderType.AVATARS,
            imageName = UUID.randomUUID().toString()
        )
    }
}
