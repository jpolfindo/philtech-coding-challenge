package com.olfindo.philtechcodingchallenge.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule that sets the main coroutine dispatcher to a test dispatcher for unit testing.
 *
 * This rule ensures that tests using coroutines run on a controlled test dispatcher, making it
 * easier to test coroutine-related logic without relying on the default `Dispatchers.Main`.
 *
 * It automatically sets up and tears down the main dispatcher for each test, preventing
 * interference between tests and ensuring consistency.
 *
 * @param testDispatcher A [TestDispatcher] to be used as the main dispatcher.
 * Defaults to [UnconfinedTestDispatcher], which executes coroutines immediately.
 *
 * Usage:
 * ```
 * @get:Rule
 * val mainCoroutineRule = MainCoroutineRule()
 * ```
 *
 * @see TestDispatcher
 * @see UnconfinedTestDispatcher
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {

    /**
     * Called before a test starts. Sets the main dispatcher to the test dispatcher.
     *
     * @param description Metadata about the test being run (optional).
     */
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Called after a test finishes. Resets the main dispatcher to its original state.
     *
     * @param description Metadata about the test being run (optional).
     */
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
