package com.olfindo.philtechcodingchallenge.ui.viewmodel

import android.accounts.NetworkErrorException
import com.olfindo.philtechcodingchallenge.data.model.CoordinatesResponse
import com.olfindo.philtechcodingchallenge.data.model.LocationResponse
import com.olfindo.philtechcodingchallenge.data.model.NameResponse
import com.olfindo.philtechcodingchallenge.data.model.PictureResponse
import com.olfindo.philtechcodingchallenge.data.model.StreetResponse
import com.olfindo.philtechcodingchallenge.data.model.TimezoneResponse
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManager
import com.olfindo.philtechcodingchallenge.testutils.MainCoroutineRule
import com.olfindo.philtechcodingchallenge.ui.viewmodels.ErrorMessages
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UserViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Unit tests for [UserViewModel] to verify various UI states and interactions.
 *
 * These tests ensure the following:
 * - Validation of user input (range validation and error handling)
 * - Correct API call behavior based on user input
 * - Correct UI state transitions based on data fetching
 * - Error handling for network and other errors
 * - Correct loading state behavior during data fetching
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userRepository: UserRepoManager = mockk()

    private lateinit var viewModel: UserViewModel

    /**
     * Setup the ViewModel before each test.
     * Mocks the repository's `getUsers` function to return an empty list by default.
     */
    @Before
    fun setup() {
        viewModel = UserViewModel(userRepository)
        coEvery { userRepository.getUsers(any()) } returns emptyList() // Default behavior
    }

    /**
     * Cleans up after each test by clearing mocks to prevent unwanted side effects.
     */
    @After
    fun teardown() {
        clearAllMocks()
    }

    /**
     * Verifies that by default, the UI state shows input.
     */
    @Test
    fun `should show input by default`() = runTest {
        assertTrue(viewModel.uiState.value.showInput)
    }

    /**
     * Verifies that the user input is validated to be between 1 and 5000.
     * Ensures that invalid inputs trigger an error message.
     */
    @Test
    fun `should validate user input is between 1 and 5000`() = runTest {
        listOf(0, 5001).forEach { invalidInput ->
            viewModel.validateInput(invalidInput)
            assertEquals(ErrorMessages.INPUT_1_TO_5000, viewModel.uiState.value.errorMessage)
        }
        coVerify(exactly = 0) { userRepository.getUsers(any()) }
    }

    /**
     * Verifies that users are fetched correctly for valid input.
     * Ensures that the repository is called and the UI state is updated with the fetched users.
     */
    @Test
    fun `should fetch users for valid input`() = runTest {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.validateInput(1)

        coVerify { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) }
        assertEquals(expectedUsers, viewModel.uiState.value.users)
    }

    /**
     * Verifies that selecting a user updates the selected user in the ViewModel.
     */
    @Test
    fun `should update selected user when selectUser is called`() {
        viewModel.selectUser(expectedUsers[0])
        assertEquals(expectedUsers[0], viewModel.selectedUser.value)
    }

    /**
     * Verifies that the repository is called only once even if multiple fetch calls are made.
     * This ensures that redundant API calls are avoided when the same data is already fetched.
     */
    @Test
    fun `should call repository only once if users are already fetched`() = runTest {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)
        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        coVerify(exactly = 1) { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) }
    }

    /**
     * Verifies that the UI state is reset correctly when `resetUIState` is called.
     * Ensures that users are cleared and UI state goes back to default.
     */
    @Test
    fun `should reset uiState and hasFetchedUsers`() {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.validateInput(EXPECTED_NUMBER_OF_RESULTS)
        viewModel.resetUIState()

        assertUIState()
    }

    /**
     * Verifies that a network error is handled correctly by updating the UI state with the appropriate error message.
     */
    @Test
    fun `should handle network error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws NetworkErrorException()

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = ErrorMessages.NETWORK_ERROR,
            isLoading = false
        )
    }

    /**
     * Verifies that a general error is handled correctly by updating the UI state with the error message.
     */
    @Test
    fun `should handle general error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws Exception(MOCK_ERROR)

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = MOCK_ERROR,
            isLoading = false
        )
    }

    /**
     * Verifies that an unknown error is handled correctly during the fetch operation.
     * The UI state is updated with the generic unknown error message.
     */
    @Test
    fun `should handle unknown error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws Exception()

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = ErrorMessages.UNKNOWN_ERROR,
            isLoading = false
        )
    }

    /**
     * Verifies that the loading state behaves as expected during a fetch operation.
     * Initially shows loading state, then updates when the fetch completes.
     */
    @Test
    fun `should show loading state during fetchUsers`() = runTest {
        // Simulate a delay in fetching users
        coEvery { userRepository.getUsers(any()) } coAnswers {
            delay(100) // Simulate network delay
            expectedUsers
        }

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        // Assert loading state is true initially
        assertEquals(true, viewModel.uiState.value.isLoading)

        // Wait for fetch to complete
        advanceUntilIdle()

        // Assert loading state is false after fetch
        assertEquals(false, viewModel.uiState.value.isLoading)

        // Assert the fetched users are correct
        assertEquals(expectedUsers, viewModel.uiState.value.users)
    }


    /**
     * Helper function to assert the UI state.
     * Verifies that the state of the UI matches the expected values.
     */
    private fun assertUIState(
        users: List<UserListResponse> = emptyList(),
        isLoading: Boolean = false,
        errorMessage: String? = null,
        showInput: Boolean = true
    ) {
        with(viewModel.uiState.value) {
            assertEquals(users, this.users)
            assertEquals(isLoading, this.isLoading)
            assertEquals(errorMessage, this.errorMessage)
            assertEquals(showInput, this.showInput)
        }
    }

    companion object {
        const val EXPECTED_NUMBER_OF_RESULTS = 1
        val expectedUsers = listOf(
            UserListResponse(
                name = NameResponse("Mr", "John", "Doe"),
                gender = "Male",
                location = LocationResponse(
                    street = StreetResponse(123, "Vincent St"),
                    city = "Quezon City",
                    state = "NCR",
                    country = "Philippines",
                    postcode = "1234",
                    coordinates = CoordinatesResponse("14.5775°N", "121.0544°E"),
                    timezone = TimezoneResponse("+08:00", "Philippine Standard Time")
                ),
                email = "coding.challenge@example.com",
                picture = PictureResponse("large_url", "medium_url", "thumbnail_url")
            )
        )
        const val MOCK_ERROR = "Mock Error"
    }
}
