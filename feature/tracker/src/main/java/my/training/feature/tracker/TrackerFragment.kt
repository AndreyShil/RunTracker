package my.training.feature.tracker

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import my.training.feature.tracker.databinding.FragmentTrackerBinding

internal class TrackerFragment : Fragment(R.layout.fragment_tracker) {

    private var _binding: FragmentTrackerBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val viewModel: TrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)
        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}