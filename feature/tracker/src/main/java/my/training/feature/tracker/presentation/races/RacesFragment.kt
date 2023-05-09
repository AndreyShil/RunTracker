package my.training.feature.tracker.presentation.races

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.core_api.di.AppWithFacade
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.FragmentRacesBinding
import my.training.feature.tracker.di.RacesComponent
import javax.inject.Inject

internal class RacesFragment : Fragment(R.layout.fragment_races) {

    @Inject
    lateinit var viewModelFactory: RacesViewModelFactory

    private val binding by viewBinding(FragmentRacesBinding::bind)
    private val viewModel: RacesViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RacesComponent
            .create((requireActivity().application as AppWithFacade).getFacade())
            .inject(this)
    }
}