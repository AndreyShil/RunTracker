package my.training.feature.tracker.presentation.races

import my.training.feature.tracker.data.repository.RaceRepository
import my.training.feature.tracker.utils.MainDispatcherRule
import my.training.feature.tracker.utils.NonexistentViewModel
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class RacesViewModelFactoryTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var raceRepository: RaceRepository

    private lateinit var factoryUnderTest: RacesViewModelFactory

    @Before
    fun setup() {
        factoryUnderTest = RacesViewModelFactory(
            raceRepository = raceRepository
        )
    }

    @Test
    fun should_be_a_instance_of_trackerViewModel() {
        val expectedViewModelClass = RacesViewModel::class.java
        val actualViewModel = factoryUnderTest.create(expectedViewModelClass)

        assertThat(actualViewModel, CoreMatchers.instanceOf(expectedViewModelClass))
    }

    @Test
    fun `should fail due to cannot instantiate exception`() {
        try {
            factoryUnderTest.create(NonexistentViewModel::class.java)
            Assert.fail("A \"Cannot instantiate NonexistentViewModel\" exception should be thrown, but it wasn't")
        } catch (t: Throwable) {
            assertThat(t, CoreMatchers.instanceOf(AssertionError::class.java))
            assertTrue(t.message?.contains("Cannot instantiate", true) == true)
        }
    }
}