package my.training.feature.tracker.presentation.tracker

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.domain.model.race.RaceCreating
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.core_api.toModel
import my.training.feature.tracker.data.repository.RaceRepository
import my.training.feature.tracker.utils.MainDispatcherRule
import my.training.feature.tracker.utils.RacesFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TrackerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var racesViewModel: TrackerViewModel

    private val raceRepository = mock<RaceRepository>()

    @Before
    fun setup() {
        racesViewModel = TrackerViewModel(raceRepository)
    }

    @Test
    fun should_update_state_and_send_effect_after_success_create_race_request() = runTest {
        whenever(raceRepository.createRace(any()))
            .thenReturn(NetworkResponse.Success(RacesFactory.getCreatedRace().toModel()))

        racesViewModel.addNewWorkout()

        val expectedState = TrackerContract.State(
            race = RaceCreating()
        )
        val actualState = racesViewModel.currentState

        val expectedEffect = TrackerContract.Effect.ShowMessage("Тренировка добавлена")
        val actualEffect = racesViewModel.effect.firstOrNull()

        assertEquals(expectedState, actualState)
        assertEquals(expectedEffect, actualEffect)
    }

    @Test
    fun should_send_effect_after_failure_connection_create_race_request() = runTest {
        whenever(raceRepository.createRace(any()))
            .thenReturn(NetworkResponse.Failure.Connection)

        racesViewModel.addNewWorkout()

        val expectedState = TrackerContract.State()
        val actualState = racesViewModel.currentState

        val expectedEffect = TrackerContract.Effect.ShowMessage(
            NetworkResponse.Failure.Connection.getErrorMessage()
        )
        val actualEffect = racesViewModel.effect.firstOrNull()

        assertEquals(expectedState, actualState)
        assertEquals(expectedEffect, actualEffect)
    }

    @Test
    fun should_send_effect_after_failure_error_create_race_request() = runTest {
        whenever(raceRepository.createRace(any()))
            .thenReturn(NetworkResponse.Failure.Error(Throwable("Ошибка авторизации")))

        racesViewModel.addNewWorkout()

        val expectedState = TrackerContract.State()
        val actualState = racesViewModel.currentState

        val expectedEffect = TrackerContract.Effect.ShowMessage("Ошибка авторизации")
        val actualEffect = racesViewModel.effect.firstOrNull()

        assertEquals(expectedState, actualState)
        assertEquals(expectedEffect, actualEffect)
    }
}