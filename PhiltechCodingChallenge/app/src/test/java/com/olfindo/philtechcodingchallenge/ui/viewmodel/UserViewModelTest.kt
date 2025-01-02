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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userRepository: UserRepoManager = mockk()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        viewModel = UserViewModel(userRepository)
        coEvery { userRepository.getUsers(any()) } returns emptyList() // Default behavior
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `should show input by default`() = runTest {
        assertTrue(viewModel.uiState.value.showInput)
    }

    @Test
    fun `should validate user input is between 1 and 5000`() = runTest {
        listOf(0, 5001).forEach { invalidInput ->
            viewModel.validateInput(invalidInput)
            assertEquals(ErrorMessages.INPUT_1_TO_5000, viewModel.uiState.value.errorMessage)
        }
        coVerify(exactly = 0) { userRepository.getUsers(any()) }
    }

    @Test
    fun `should fetch users for valid input`() = runTest {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.validateInput(1)

        coVerify { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) }
        assertEquals(expectedUsers, viewModel.uiState.value.users)
    }

    @Test
    fun `should update selected user when selectUser is called`() {
        viewModel.selectUser(expectedUsers[0])
        assertEquals(expectedUsers[0], viewModel.selectedUser.value)
    }

    @Test
    fun `should call repository only once if users are already fetched`() = runTest {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)
        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        coVerify(exactly = 1) { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) }
    }

    @Test
    fun `should reset uiState and hasFetchedUsers`() {
        coEvery { userRepository.getUsers(EXPECTED_NUMBER_OF_RESULTS) } returns expectedUsers

        viewModel.validateInput(EXPECTED_NUMBER_OF_RESULTS)
        viewModel.resetUIState()

        assertUIState()
    }

    @Test
    fun `should handle network error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws NetworkErrorException()

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = ErrorMessages.NETWORK_ERROR,
            isLoading = false
        )
    }

    @Test
    fun `should handle general error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws Exception(MOCK_ERROR)

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = MOCK_ERROR,
            isLoading = false
        )
    }

    @Test
    fun `should handle unknown error during fetchUsers`() = runTest {
        coEvery { userRepository.getUsers(any()) } throws Exception()

        viewModel.fetchUsers(EXPECTED_NUMBER_OF_RESULTS)

        assertUIState(
            errorMessage = ErrorMessages.UNKNOWN_ERROR,
            isLoading = false
        )
    }

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
