package my.training.feature.tracker.presentation.tracker

import android.Manifest
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.launch
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
    private var racePolyline = Polyline()
    private var raceTime = 0L

    private var routeStartLocation: Point? = null
    private var userLocationLayer: UserLocationLayer? = null
    private var followUserLocation = true

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as RunningService.LocationBinder).apply {
                subscribeToLocations { locations ->
                    addPolyline(locations)
                }
                unbindService {
                    unbindService()
                }
                getService().timeRunInMillis.observe(viewLifecycleOwner) {
                    if (it != 0L) {
                        raceTime = it
                    }
                    setTimerValue()
                }
                getService().state.observe(viewLifecycleOwner) {
                    controlBinding?.viewControl?.setState(it)
                }
            }
            mIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIsBound = false
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
        followUserLocation = savedInstanceState?.getBoolean(FOLLOW_USER_LOCATION_KEY) ?: true
        raceTime = savedInstanceState?.getLong(RACE_TIME_KEY) ?: 0L
    }

    private val locationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            launchStartIntent()
            onMapReady()
        } else {
            showSnackbar(
                getString(my.training.core.strings.R.string.location_permission_is_needed_for_app)
            )
        }
    }

    private val myLocationContract = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            onMapReady()
        } else {
            showSnackbar(
                getString(my.training.core.strings.R.string.location_permission_is_needed_for_app)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlBinding = ViewTrackingControlBinding.bind(binding.root)
        controlBinding?.viewControl?.setListener(controlListener)

        binding.mapView.map?.isNightModeEnabled = requireContext().isNightModeActive()

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
        initUiEffectObserver()
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is TrackerContract.Effect.ShowMessage -> {
                            showSnackbar(effect.message)
                        }
                    }
                }
            }
        }
    }

    private val controlListener = object : ControlView.ControlListener {
        override fun onStartClicked() {
            onStartTrainingClicked()
        }

        override fun onPauseClicked() {
            launchPauseIntent()
        }

        override fun onStopClicked() {
            launchStopIntent()
        }

        override fun onFinishClicked() {
            binding.mapView.screenshot?.let {
                uploadTrackImage(it)
            }
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

    private fun uploadTrackImage(bitmap: Bitmap) {
        firebaseStorageManager.uploadImage(
            bitmap = bitmap,
            folderName = "races",
            imageName = UUID.randomUUID().toString(),
            failureListener = { exception ->
                showSnackbar(
                    exception.message
                        ?: getString(my.training.core.strings.R.string.error_image_download)
                )
                resetParams()
            },
            successfulListener = { url ->
                viewModel.setEvent(
                    TrackerContract.Event.OnMapScreenDownloaded(
                        mapScreenUrl = url
                    )
                )
                resetParams()
            }
        )
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

    private fun addPolyline(locations: List<Location>) {
        if (locations.isEmpty()) return

        viewModel.setEvent(
            TrackerContract.Event.OnWorkoutFinished(
                distance = calculateDistance(locations),
                time = raceTime / 1000
            )
        )

        racePolyline = Polyline(locations.map { Point(it.latitude, it.longitude) })
        binding.mapView.map?.mapObjects?.addPolyline(racePolyline).apply {
            this?.strokeWidth = POLYLINE_STROKE_WIDTH
        }
        moveCameraByPolylinePosition()
        setMapEnabled(false)
    }

    private fun moveCameraByPolylinePosition() {
        var cameraPosition = binding.mapView.map.cameraPosition(
            Geometry.fromPolyline(racePolyline), null, null, null
        )

        cameraPosition = CameraPosition(
            cameraPosition.target,
            cameraPosition.zoom * 0.99f,
            cameraPosition.azimuth,
            cameraPosition.tilt
        )
        binding.mapView.map.move(cameraPosition)
    }

    private fun setMapEnabled(isEnabled: Boolean) {
        binding.mapView.map.apply {
            isFastTapEnabled = isEnabled
            isRotateGesturesEnabled = isEnabled
            isScrollGesturesEnabled = isEnabled
            isZoomGesturesEnabled = isEnabled
            isTiltGesturesEnabled = isEnabled
        }
    }

    private fun resetParams() {
        setMapEnabled(true)
        binding.mapView.map?.mapObjects?.clear()
        raceTime = 0L
        setTimerValue()
    }

    private fun calculateDistance(locations: List<Location>): Double {
        var distance = 0.0

        for (index in 0 until locations.lastIndex) {
            val startLocation = locations[index]
            val endLocation = locations[index + 1]
            distance += startLocation.distanceTo(endLocation)
        }
        return distance
    }

    private fun setTimerValue() {
        controlBinding?.tvTimer?.text = raceTime.getFormattedWatchTime(true)
    }

    /**
     * User location layer methods
     */
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
        outState.putBoolean(FOLLOW_USER_LOCATION_KEY, followUserLocation)
        outState.putLong(RACE_TIME_KEY, raceTime)
    }

    override fun onDestroyView() {
        controlBinding = null
        super.onDestroyView()
    }

    companion object {
        private const val IS_BOUND_KEY = "is_bound_key"
        private const val RACE_TIME_KEY = "race_time_key"
        private const val FOLLOW_USER_LOCATION_KEY = "follow_user_location_key"

        private const val DEFAULT_ZOOM = 16f
        private const val DEFAULT_CAMERA_ANIMATION = 1f
        private const val DEFAULT_AZIMUTH = 0f
        private const val DEFAULT_TILT = 0f
        private const val POLYLINE_STROKE_WIDTH = 3f
    }
}
