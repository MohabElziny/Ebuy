package com.iti.android.team1.ebuy

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        assertThat((2 + 2), `is`(4))
        assertThat((2 + 2), not(3))
        runTest {

        }
    }
}