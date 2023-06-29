package my.training.feature.tracker.data.repository

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.network.RacesApiService
import my.training.core.core_api.toModel
import my.training.feature.tracker.utils.RacesFactory
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

class RaceRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val racesApiService = mock<RacesApiService>()

    private val repository = RaceRepositoryImpl(
        dispatcherIO = testDispatcher,
        racesApiService = racesApiService
    )

    @Test
    fun should_return_success_add_race_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.addRace(any()))
            .thenReturn(Response.success(RacesFactory.getCreatedRace()))

        val expected = NetworkResponse.Success(RacesFactory.getCreatedRace().toModel())
        val actual = repository.createRace(RacesFactory.getCreateRaceRequestData())

        assertEquals(expected, actual)
        verify(racesApiService).addRace(any())
    }

    @Test
    fun should_return_failure_by_error_add_race_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.addRace(any()))
            .thenReturn(Response.error(401, "Ошибка авторизации".toResponseBody()))

        val actual = repository.createRace(RacesFactory.getCreateRaceRequestData())

        assertTrue(actual is NetworkResponse.Failure.Error)
        verify(racesApiService).addRace(any())
    }

    @Test
    fun should_return_failure_by_connection_add_race_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.addRace(any()))
            .thenThrow(HttpException::class.java)

        val actual = repository.createRace(RacesFactory.getCreateRaceRequestData())

        assertTrue(actual is NetworkResponse.Failure.Connection)
        verify(racesApiService).addRace(any())
    }

    @Test
    fun should_return_success_races_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.getRaces())
            .thenReturn(Response.success(RacesFactory.getRaces()))

        val expected = NetworkResponse.Success(RacesFactory.getRaces().map { it.toModel() })
        val actual = repository.getRaces()

        assertEquals(actual, expected)
        verify(racesApiService).getRaces()
    }

    @Test
    fun should_return_failure_by_error_races_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.getRaces())
            .thenReturn(Response.error(401, "Ошибка авторизации".toResponseBody()))

        val actual = repository.getRaces()

        assertTrue(actual is NetworkResponse.Failure.Error)
        verify(racesApiService).getRaces()
    }

    @Test
    fun should_return_failure_by_connection_races_network_response() = runTest(testDispatcher) {
        whenever(racesApiService.getRaces())
            .thenThrow(HttpException::class.java)

        val actual = repository.getRaces()

        assertTrue(actual is NetworkResponse.Failure.Connection)
        verify(racesApiService).getRaces()
    }
}
