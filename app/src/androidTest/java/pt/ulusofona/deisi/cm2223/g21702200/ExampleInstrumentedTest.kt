package pt.ulusofona.deisi.cm2223.g21702200

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.projectcm.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


@RunWith(AndroidJUnit4::class)
class TestApplication {

    //private lateinit var app: ProjectApplication

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.projectcm", appContext.packageName)
    }

    @Test
    fun press2Then2ShouldBeDisplayed2() {
        //println("navigation -> " + withId(R.id.navigation_dashboard))
        onView(withId(R.id.navigation_dashboard)).check(matches(withText("Dashboard")))
    }
}

