package my.training.feature.stats.presentation.stats_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.ui.extensions.showSnackbar
import my.training.feature.stats.R
import my.training.feature.stats.databinding.FragmentStatsBinding
import my.training.feature.stats.di.StatsComponent
import javax.inject.Inject

internal class StatsFragment : Fragment(R.layout.fragment_stats) {

    @Inject
    lateinit var viewModelFactory: StatsViewModelFactory

    private val viewModel: StatsViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentStatsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatsComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
        initUiEffectObserver()

        binding.viewStats.setUpdateListener(viewModel.listener)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (!it.isLoading)
                        binding.viewStats.setData(it.stats)
                }
            }
        }
    }

    private fun initUiEffectObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is StatsContract.Effect.ShowError -> {
                            showSnackbar(effect.errorMessage)
                        }
                    }
                }
            }
        }
    }


}