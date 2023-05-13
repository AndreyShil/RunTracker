package my.training.feature.tracker.presentation.tracker

import android.Manifest
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.core_api.extensions.isNightModeActive
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.FragmentTrackerBinding
import my.training.feature.tracker.di.TrackerComponent
import my.training.feature.tracker.domain.service.RunningService
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject


internal class TrackerFragment : Fragment(R.layout.fragment_tracker) {

    @Inject
    lateinit var viewModelFactory: TrackerViewModelFactory

    private val binding by viewBinding(FragmentTrackerBinding::bind)
    private val viewModel: TrackerViewModel by viewModels { viewModelFactory }

    var mIsBound: Boolean? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as RunningService.LocationBinder).apply {
                subscribeToLocations { locations ->
                    Log.d("LOCATIONS_CONNECTION", locations.toString())
                    Log.d("LOCATIONS_CONNECTION", "count - ${locations.count()}")
                }
                unbindService {
                    unbindService()
                }
            }
            mIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
            Log.d("LOCATIONS_CONNECTION", "Disconnect")
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TrackerComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)

        MapKitFactory.initialize(requireContext())
    }

    private val locationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            launchStartIntent()
        } else {
            showSnackbar("Для работы приложения необходимо разрешения на доступ к местоположению")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val points = listOf(
            Point(56.317827, 44.024141),
            Point(56.32, 44.03),
            Point(56.33, 44.04)
        )
        val polyline = Polyline(points)
        binding.mapView.map?.mapObjects?.addPolyline(polyline).apply {
            this?.strokeWidth = 3f
        }

        binding.mapView.map?.isNightModeEnabled = requireContext().isNightModeActive()


        binding.fabScreenshot.setOnClickListener {
            binding.mapView.screenshot?.let {
                uploadImage(it)
            }

        }

        var cameraPosition = binding.mapView.map.cameraPosition(
            Geometry.fromPolyline(polyline), null, null, null
        )

        cameraPosition = CameraPosition(
            cameraPosition.target,
            cameraPosition.zoom * 0.99f,
            cameraPosition.azimuth,
            cameraPosition.tilt
        )
        binding.mapView.map.move(cameraPosition)


        binding.fabMyLocation.setOnClickListener {
            binding.mapView.map?.move(
                CameraPosition(
                    Point(56.317827, 44.024141),
                    15.0f,
                    0.0f,
                    0.0f
                ),
                Animation(Animation.Type.SMOOTH, 2f),
                null
            )
        }



        binding.run {
            fabStartLocation.setOnClickListener {
                onStartTrainingClicked()
            }
            fabStopLocation.setOnClickListener {
                launchStopIntent()
            }
        }
    }

    private fun launchStartIntent() {
        Intent(requireActivity().applicationContext, RunningService::class.java).apply {
            action = RunningService.ACTION_START_OR_RESUME_SERVICE
            requireActivity().startService(this)
        }
        bindService()
    }

    private fun launchStopIntent() {
        Intent(requireActivity().applicationContext, RunningService::class.java).apply {
            action = RunningService.ACTION_STOP_SERVICE
            requireActivity().startService(this)
        }
        unbindService()
    }

    private fun onStartTrainingClicked() {
        locationContract.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun uploadImage(bitmap: Bitmap) {
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child("races/" + UUID.randomUUID().toString())

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        ref.putBytes(bytes)
            .addOnSuccessListener { // Image uploaded successfully
                Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { taskSnapshot ->

            }.addOnCompleteListener {
                Toast.makeText(requireContext(), "Result - ${it.result}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bindService() {
        Intent(requireContext(), RunningService::class.java).also {
            activity?.bindService(it, connection, BIND_AUTO_CREATE)
        }
        mIsBound = true
    }

    private fun unbindService() {
        if (mIsBound == true) {
            activity?.unbindService(connection)
            mIsBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        bindService()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        unbindService()
    }

}