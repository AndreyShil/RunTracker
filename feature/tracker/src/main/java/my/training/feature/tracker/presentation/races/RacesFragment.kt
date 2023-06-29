package my.training.feature.tracker.presentation.races

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.FragmentRacesBinding
import my.training.feature.tracker.di.RacesComponent
import my.training.feature.tracker.presentation.races.adapter.RacesAdapter
import javax.inject.Inject

internal class RacesFragment : Fragment(R.layout.fragment_races) {

    @Inject
    lateinit var viewModelFactory: RacesViewModelFactory

    @Inject
    lateinit var racesAdapter: RacesAdapter

    private val binding by viewBinding(FragmentRacesBinding::bind)
    private val viewModel: RacesViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RacesComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRaces.adapter = racesAdapter.apply {
            setListener { raceId ->
                viewModel.setEvent(
                    RacesContract.Event.UpdateRacesList(raceId)
                )
            }
        }

        observeUiState()
        initUiEffectObserver()
        binding.fabAdd.setOnClickListener {
            openTrackerScreen()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    racesAdapter.submitList(it.races)
                }
            }
        }
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is RacesContract.Effect.ShowError -> {
                            showSnackbar(effect.errorMessage)
                        }
                    }
                }
            }
        }
    }

    private fun openTrackerScreen() {
        val action = RacesFragmentDirections.actionRacesFragmentToTrackerFragment()
        findNavController().navigate(action)
    }
}