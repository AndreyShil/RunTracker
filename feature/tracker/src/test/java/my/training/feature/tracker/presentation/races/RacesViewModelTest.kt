package my.training.feature.tracker.presentation.races

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.extensions.getErrorMessage
import my.training.feature.tracker.data.mapper.toRaceModelList
import my.training.feature.tracker.data.repository.RaceRepository
import my.training.feature.tracker.utils.MainDispatcherRule
import my.training.feature.tracker.utils.RacesFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RacesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var racesViewModel: RacesViewModel

    private val raceRepository = mock<RaceRepository>()

    @Before
    fun setup() {
        racesViewModel = RacesViewModel(raceRepository)
    }

    @Test
    fun should_update_state_after_success_races_request() = runTest {
        whenever(raceRepository.getRaces())
            .thenReturn(NetworkResponse.Success(RacesFactory.getRacesModel()))

        racesViewModel.loadRaces()

        val expectedState = RacesContract.State(
            races = RacesFactory.getRacesModel().toRaceModelList()
        )
        val actualState = racesViewModel.currentState
        assertEquals(expectedState, actualState)
    }

    @Test
    fun should_send_effect_after_failure_connection_races_request() = runTest {
        whenever(raceRepository.getRaces())
            .thenReturn(NetworkResponse.Failure.Connection)

        racesViewModel.loadRaces()

        val expectedState = RacesContract.State()
        val actualState = racesViewModel.currentState

        val expectedEffect = RacesContract.Effect.ShowError(
            NetworkResponse.Failure.Connection.getErrorMessage()
        )
        val actualEffect = racesViewModel.effect.firstOrNull()

        assertEquals(expectedState, actualState)
        assertEquals(expectedEffect, actualEffect)
    }

    @Test
    fun should_send_effect_after_failure_error_races_request() = runTest {
        whenever(raceRepository.getRaces())
            .thenReturn(NetworkResponse.Failure.Error(Throwable("Ошибка авторизации")))

        racesViewModel.loadRaces()

        val expectedState = RacesContract.State()
        val actualState = racesViewModel.currentState

        val expectedEffect = RacesContract.Effect.ShowError("Ошибка авторизации")
        val actualEffect = racesViewModel.effect.firstOrNull()

        assertEquals(expectedState, actualState)
        assertEquals(expectedEffect, actualEffect)
    }
}
