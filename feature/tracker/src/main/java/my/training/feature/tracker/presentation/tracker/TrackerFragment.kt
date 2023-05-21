package my.training.feature.tracker.presentation.tracker

import android.Manifest
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.core.core_api.extensions.isNightModeActive
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.FragmentTrackerBinding
import my.training.feature.tracker.databinding.ViewTrackingControlBinding
import my.training.feature.tracker.di.TrackerComponent
import my.training.feature.tracker.domain.service.RunningService
import my.training.feature.tracker.extension.getFormattedWatchTime
import my.training.feature.tracker.extension.hasLocationPermission
import java.util.UUID
import javax.inject.Inject

internal class TrackerFragment : Fragment(R.layout.fragment_tracker), UserLocationObjectListener,
    CameraListener {

    @Inject
    lateinit var viewModelFactory: TrackerViewModelFactory

    @Inject
    lateinit var firebaseStorageManager: FirebaseStorageManager

    private val binding by viewBinding(FragmentTrackerBinding::bind)
    private val viewModel: TrackerViewModel by viewModels { viewModelFactory }

    private var controlBinding: ViewTrackingControlBinding? = null

    private var mIsBound: Boolean? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as RunningService.LocationBinder).apply {
                subscribeToLocations { locations ->
                    Log.d("LOCATIONS_CONNECTION", locations.toString())
                    Log.d("LOCATIONS_CONNECTION", "count - ${locations.count()}")

                    addPolyline(locations)
                }
                unbindService {
                    unbindService()
                }

                getService().timeRunInMillis.observe(viewLifecycleOwner) {
                    controlBinding?.tvTimer?.text = it.getFormattedWatchTime(true)
                }
                getService().state.observe(viewLifecycleOwner) {
                    controlBinding?.viewControl?.setState(it)
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
        mIsBound = savedInstanceState?.getBoolean(IS_BOUND_KEY)
    }

    private val locationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            launchStartIntent()
            onMapReady()
        } else {
            showSnackbar("Для работы приложения необходимо разрешение на доступ к местоположению")
        }
    }

    private val myLocationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            onMapReady()
        } else {
            showSnackbar("Для работы приложения необходимо разрешение на доступ к местоположению")
        }
    }

    private fun addPolyline(locations: List<Location>) {
        if (locations.isEmpty()) return

        val polyline = Polyline(locations.map { Point(it.latitude, it.longitude) })
        binding.mapView.map?.mapObjects?.addPolyline(polyline).apply {
            this?.strokeWidth = POLYLINE_STROKE_WIDTH
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
    }

    private var routeStartLocation: Point? = null
    private var userLocationLayer: UserLocationLayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlBinding = ViewTrackingControlBinding.bind(binding.root)
        controlBinding?.viewControl?.setListener(controlListener)

        binding.mapView.map?.isNightModeEnabled = requireContext().isNightModeActive()

        binding.loadImage.setOnClickListener {
            binding.mapView.screenshot?.let {
                firebaseStorageManager.uploadImage(
                    bitmap = it,
                    folderName = "races",
                    imageName = UUID.randomUUID().toString(),
                    failureListener = { exception ->
                        showSnackbar(exception.message ?: "Ошибка загрузки")
                    },
                    successfulListener = { url ->
                        viewModel.setEvent(
                            TrackerContract.Event.OnMapScreenDownloaded(
                                mapScreenUrl = url
                            )
                        )
                    }
                )
            }

        }

        binding.fabMyLocation.setOnClickListener {
            myLocationContract.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
        if (requireContext().hasLocationPermission()) {
            onMapReady()
        }
    }

    // TODO: Remove
    private fun createMockPolyline() {
        val points = listOf(
            Point(56.312746, 44.028540),
            Point(56.315316, 44.026028),
            Point(56.318049, 44.023429),
            Point(56.319367, 44.028716),
            Point(56.320841, 44.031806),
            Point(56.321660, 44.034110),
            Point(56.321035, 44.037490),
            Point(56.321714, 44.038517),
            Point(56.320672, 44.047128)
        )

        Log.d("DISTANCE", calculateDistance(points).toString())

        viewModel.setEvent(
            TrackerContract.Event.OnWorkoutFinished(
                distance = calculateDistance(points),
                time = 1680
            )
        )

        val polyline = Polyline(points)
        binding.mapView.map?.mapObjects?.addPolyline(polyline).apply {
            this?.strokeWidth = POLYLINE_STROKE_WIDTH
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
    }

    private fun calculateDistance(points: List<Point>): Double {
        var distance = 0.0

        for (index in 0 until points.lastIndex) {
            val startLocation = Location("start").apply {
                latitude = points[index].latitude
                longitude = points[index].longitude
            }
            val endLocation = Location("end").apply {
                latitude = points[index + 1].latitude
                longitude = points[index + 1].longitude
            }
            distance += startLocation.distanceTo(endLocation)
        }
        return distance
    }

    private fun onMapReady() {
        if (userLocationLayer == null) {
            val mapKit = MapKitFactory.getInstance()
            userLocationLayer = mapKit.createUserLocationLayer(binding.mapView.mapWindow)
            userLocationLayer?.isVisible = true
            userLocationLayer?.isHeadingEnabled = true
            userLocationLayer?.setObjectListener(this)

            binding.mapView.map.addCameraListener(this)
        }

        showUserPosition()
    }

    private fun showUserPosition() {
        if (userLocationLayer?.cameraPosition() != null) {
            routeStartLocation = userLocationLayer?.cameraPosition()?.target ?: return
            binding.mapView.map.move(
                CameraPosition(
                    routeStartLocation ?: return,
                    DEFAULT_ZOOM,
                    DEFAULT_AZIMUTH,
                    DEFAULT_TILT
                ),
                Animation(Animation.Type.SMOOTH, DEFAULT_CAMERA_ANIMATION),
                null
            )
        }
    }

    private var followUserLocation = true

    override fun onCameraPositionChanged(
        map: Map,
        cPos: CameraPosition,
        cUpd: CameraUpdateReason,
        finish: Boolean
    ) {
        if (finish) {
            if (followUserLocation) {
                setAnchor()
            }
        } else {
            if (!followUserLocation) {
                noAnchor()
            }
        }
    }

    private fun setAnchor() {
        userLocationLayer?.setAnchor(
            PointF(
                binding.mapView.width * 0.5f,
                binding.mapView.height * 0.5f
            ),
            PointF(
                binding.mapView.width * 0.5f,
                binding.mapView.height * 0.8f
            )
        )
        showUserPosition()
        followUserLocation = false
    }

    private fun noAnchor() {
        userLocationLayer?.resetAnchor()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        setAnchor()
        userLocationView.pin.setIcon(
            ImageProvider.fromResource(
                requireContext(),
                R.drawable.ic_user_target,
                true
            )
        )
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                requireContext(),
                R.drawable.ic_user_target,
                true
            )
        )
        userLocationView.accuracyCircle.fillColor = ContextCompat.getColor(
            requireContext(),
            R.color.accuracy_color
        )
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) = Unit
    override fun onObjectRemoved(p0: UserLocationView) = Unit


    private val controlListener = object : ControlView.ControlListener {
        override fun onStartClicked() {
            createMockPolyline()
//            onStartTrainingClicked()
        }

        override fun onPauseClicked() {
            launchPauseIntent()
        }

        override fun onStopClicked() {
            launchStopIntent()
        }
    }

    private fun onStartTrainingClicked() {
        locationContract.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun launchStartIntent() {
        Intent(requireActivity().applicationContext, RunningService::class.java).apply {
            action = RunningService.ACTION_START_OR_RESUME_SERVICE
            requireActivity().startService(this)
        }
        bindService()
    }

    private fun launchPauseIntent() {
        Intent(requireActivity().applicationContext, RunningService::class.java).apply {
            action = RunningService.ACTION_PAUSE_SERVICE
            requireActivity().startService(this)
        }
        unbindService()
    }

    private fun launchStopIntent() {
        Intent(requireActivity().applicationContext, RunningService::class.java).apply {
            action = RunningService.ACTION_STOP_SERVICE
            requireActivity().startService(this)
        }
        unbindService()
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_BOUND_KEY, mIsBound ?: false)
    }

    override fun onDestroyView() {
        controlBinding = null
        super.onDestroyView()
    }

    companion object {
        private const val IS_BOUND_KEY = "is_bound_key"
        private const val DEFAULT_ZOOM = 16f
        private const val DEFAULT_CAMERA_ANIMATION = 1f
        private const val DEFAULT_AZIMUTH = 0f
        private const val DEFAULT_TILT = 0f
        private const val POLYLINE_STROKE_WIDTH = 3f
    }

}