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
import my.training.core.core_api.domain.model.user.User
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.feature.profile.R
import my.training.feature.profile.databinding.FragmentProfileBinding
import my.training.feature.profile.di.ProfileMainComponent
import java.util.UUID
import javax.inject.Inject

internal class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var firebaseStorageManager: FirebaseStorageManager

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProfileMainComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProfileFlow()

        binding.avatar.setOnClickListener {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun observeProfileFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileFlow.collect {
                    updateUserInfo(it ?: User())
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

    private fun displayImageFromFirebaseStorage(imageUrl: String?) {
        imageUrl ?: return
        firebaseStorageManager.downloadImage(imageUrl) {
            binding.avatar.load(
                data = it
            ) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            uploadImage(it)
        }
    }

    private fun uploadImage(fileUri: Uri) {
        firebaseStorageManager.uploadImage(
            fileUri = fileUri,
            folderName = "avatars",
            imageName = UUID.randomUUID().toString()
        )
    }

}