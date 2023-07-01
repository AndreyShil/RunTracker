package my.training.runtracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import my.training.runtracker.presentation.main_activity.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import my.training.core.ui.R as uiR
import my.training.feature.auth.R as authR
import my.training.feature.profile.R as profileR

class UserAuthTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var idlingResource: CountingIdlingResource

    @Before
    fun init() {
        activityRule.scenario.onActivity {
            ((it.application as TestApp).getAggregatingProvider() as TestAggregatingComponent)
                .inject(this)
        }
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun signUpFlow() {
        onView(withId(authR.id.login_text_field)).perform(typeText("test_login"))
        onView(withId(authR.id.password_text_field)).perform(typeText("password"))

        onView(withId(authR.id.btn_sign_in)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Пользователь не найден")))

        onView(withId(authR.id.tv_register)).perform(click())

        onView(withId(authR.id.login_text_field)).perform(typeText("test_login"))
        onView(withId(authR.id.first_name_text_field)).perform(typeText("John"))
        onView(withId(authR.id.last_name_text_field)).perform(typeText("Smith"))
        onView(withId(authR.id.email_text_field)).perform(typeText("test@yandex.ru"))
        onView(withId(authR.id.scroll_view)).perform(swipeUp())
        onView(withId(authR.id.password_text_field)).perform(typeText("password"))
        onView(withId(authR.id.repeat_password_text_field)).perform(typeText("password"))

        onView(withId(authR.id.btn_sign_up)).perform(click())

        Thread.sleep(300)

        onView(withId(profileR.id.profile_graph)).perform(click())

        onView(withId(profileR.id.tv_name))
            .check(matches(withText("John Smith")))

        onView(withId(profileR.id.btn_light_theme)).perform(click())

        onView(withId(profileR.id.tv_app_theme))
            .check(matches(hasTextColor(uiR.color.md_theme_light_onSurface)))

        onView(withId(profileR.id.btn_dark_theme)).perform(click())

        onView(withId(profileR.id.tv_app_theme))
            .check(matches(hasTextColor(uiR.color.md_theme_dark_onSurface)))
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}