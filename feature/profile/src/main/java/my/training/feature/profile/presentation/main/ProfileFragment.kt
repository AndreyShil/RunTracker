package my.training.feature.profile.presentation.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.di.AppWithFacade
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.profile.R
import my.training.feature.profile.databinding.FragmentProfileBinding
import my.training.feature.profile.di.ProfileMainComponent
import java.util.UUID
import javax.inject.Inject

internal class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProfileMainComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
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
        FirebaseStorage
            .getInstance()
            .getReferenceFromUrl(imageUrl)
            .downloadUrl
            .addOnSuccessListener {
                binding.avatar.load(
                    data = it
                ) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }.addOnFailureListener {
                it.printStackTrace()
                showSnackbar(
                    it.message ?: getString(my.training.core.strings.R.string.error_image_download)
                )
            }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            uploadImage(it)
        }
    }

    private fun uploadImage(fileUri: Uri) {
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child("images/" + UUID.randomUUID().toString())

        ref.putFile(fileUri)
            .addOnSuccessListener { // Image uploaded successfully
                Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { taskSnapshot ->

            }.addOnCompleteListener {
                Log.d("RESULT_TAG", it.result.uploadSessionUri.toString())
                Log.d("RESULT_TAG", it.result.metadata?.path.toString())
                Log.d("RESULT_TAG", it.result.metadata?.name.toString())

                Toast.makeText(requireContext(), "Result - ${it.result}", Toast.LENGTH_SHORT).show()
            }
    }

}