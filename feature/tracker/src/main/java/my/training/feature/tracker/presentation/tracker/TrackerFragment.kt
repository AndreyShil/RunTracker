package my.training.feature.tracker.presentation.tracker

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import my.training.core.core_api.di.AppWithFacade
import my.training.core.core_api.extensions.isNightModeActive
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.FragmentTrackerBinding
import my.training.feature.tracker.di.TrackerComponent
import javax.inject.Inject

internal class TrackerFragment : Fragment(R.layout.fragment_tracker) {

    @Inject
    lateinit var viewModelFactory: TrackerViewModelFactory

    private val binding by viewBinding(FragmentTrackerBinding::bind)
    private val viewModel: TrackerViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TrackerComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)

        MapKitFactory.initialize(requireContext())
    }

    private val locationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            Toast.makeText(requireContext(), "isGranted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Not granted!", Toast.LENGTH_LONG).show()
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
        binding.mapView.map?.move(
            CameraPosition(
                Point(56.317827, 44.024141),
                15.0f,
                0.0f,
                0.0f
            )
        )

        binding.mapView.map?.isNightModeEnabled = requireContext().isNightModeActive()

        view.findViewById<FloatingActionButton>(R.id.action_button).setOnClickListener {
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

        locationContract.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}