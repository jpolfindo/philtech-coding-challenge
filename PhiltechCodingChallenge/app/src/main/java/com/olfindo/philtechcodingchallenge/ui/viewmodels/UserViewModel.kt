package com.olfindo.philtechcodingchallenge.ui.viewmodels

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.data.model.orDefault
import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManager
import com.olfindo.philtechcodingchallenge.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

/**
 * ViewModel for managing the UI state related to users.
 * Handles fetching user data, selecting a user, validating input, and error handling.
 * Uses [UserRepoManager] to interact with the data layer.
 *
 * @constructor Injects [UserRepoManager] for data fetching and managing user data.
 */
@HiltViewModel
open class UserViewModel @Inject constructor(
    private val userRepository: UserRepoManager
) : ViewModel() {

    // State for holding the UI state related to the user list and loading state.
    private val _uiState = MutableStateFlow(UsersUiState())
    open val uiState: StateFlow<UsersUiState> = _uiState

    // State for holding the selected user.
    private val _selectedUser = mutableStateOf<UserListResponse?>(null)
    val selectedUser: State<UserListResponse?> = _selectedUser

    /**
     * Returns the safely unwrapped selected user or a default empty user.
     */
    val safeSelectedUser: UserListResponse
        get() = _selectedUser.value.orDefault()

    // Job for controlling the fetch users operation.
    private var fetchJob: Job? = null

    /**
     * Fetches a list of users from the repository.
     * Cancels any ongoing fetch job and starts a new one.
     *
     * @param count The number of users to fetch.
     */
    open fun fetchUsers(count: Int) {
        fetchJob?.cancel() // Cancel any ongoing job before starting a new one.

        // Start a new fetch job
        fetchJob = viewModelScope.launch {
            updateUIState(isLoading = true, showInput = false)
            try {
                // Attempt to fetch users with a timeout of 30000ms (30 seconds)
                val result = withTimeout(Constants.REQUEST_TIMEOUT) {
                    userRepository.getUsers(count)
                }
                updateUIState(users = result, isLoading = false)
            } catch (e: TimeoutCancellationException) {
                handleError(e, ErrorType.TIMEOUT)
            } catch (e: NetworkErrorException) {
                handleError(e, ErrorType.NETWORK)
            } catch (e: Exception) {
                handleError(e, ErrorType.UNKNOWN)
            }
        }
    }

    /**
     * Sets the selected user.
     *
     * @param user The user to be selected.
     */
    open fun selectUser(user: UserListResponse) {
        _selectedUser.value = user
    }

    /**
     * Validates the input before attempting to fetch users.
     * Only allows fetching if the count is between 1 and 5000.
     *
     * @param count The number of users to fetch.
     */
    open fun validateInput(count: Int) {
        if (count in Constants.RANGE) {
            updateUIState(errorMessage = null)
            fetchUsers(count)
        } else {
            updateUIState(errorMessage = ErrorMessages.INPUT_1_TO_5000)
        }
    }

    /**
     * Resets the UI state to its initial state.
     */
    open fun resetUIState() {
        _uiState.value = UsersUiState()
        _selectedUser.value = null
    }

    /**
     * Updates the UI state with the provided values.
     *
     * @param users The list of users to display.
     * @param isLoading Whether the UI should display a loading state.
     * @param errorMessage The error message to display (if any).
     * @param showInput Whether the input field should be visible.
     */
    private fun updateUIState(
        users: List<UserListResponse> = emptyList(),
        isLoading: Boolean = false,
        errorMessage: String? = null,
        showInput: Boolean = true
    ) {
        _uiState.value = _uiState.value.copy(
            users = users,
            isLoading = isLoading,
            errorMessage = errorMessage,
            showInput = showInput
        )
    }

    /**
     * Handles errors by updating the UI state and logging the error.
     *
     * @param exception The exception that was thrown.
     * @param errorType The type of error that occurred.
     */
    private fun handleError(exception: Exception, errorType: ErrorType) {
        val errorMessage = when (errorType) {
            ErrorType.NETWORK -> ErrorMessages.NETWORK_ERROR
            ErrorType.TIMEOUT -> ErrorMessages.TIMEOUT_ERROR
            ErrorType.PARSING -> ErrorMessages.PARSING_ERROR
            ErrorType.AUTHENTICATION -> ErrorMessages.AUTHENTICATION_ERROR
            ErrorType.UNKNOWN -> exception.message ?: ErrorMessages.UNKNOWN_ERROR
        }

        // Update the UI state with an error message
        updateUIState(isLoading = false, showInput = true, errorMessage = errorMessage)

        // Log the error for debugging or monitoring purposes
        logError(exception, errorType)
    }

    /**
     * Logs errors for debugging or analytics purposes.
     *
     * @param exception The exception to log.
     * @param errorType The type of error that occurred.
     */
    private fun logError(exception: Exception, errorType: ErrorType) {
        Log.e("Error Type: $errorType", "Message: ${exception.message}")
    }

    /**
     * Enum representing different types of errors that can occur.
     */
    private enum class ErrorType {
        NETWORK, TIMEOUT, PARSING, AUTHENTICATION, UNKNOWN
    }
}

/**
 * Data class representing the UI state of the users screen.
 *
 * @property users A list of users to be displayed.
 * @property isLoading Whether the UI is in a loading state.
 * @property errorMessage Any error message to display.
 * @property showInput Whether the input field should be visible.
 */
data class UsersUiState(
    val users: List<UserListResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showInput: Boolean = true
)

/**
 * Object containing error messages for different scenarios.
 */
object ErrorMessages {
    const val NETWORK_ERROR = "Network error. Please check your internet connection."
    const val TIMEOUT_ERROR = "The request timed out. Please try again later."
    const val PARSING_ERROR = "Data could not be processed. Please try again."
    const val AUTHENTICATION_ERROR = "Authentication failed. Please check your credentials."
    const val UNKNOWN_ERROR = "An unknown error occurred. Please contact support."
    const val INPUT_1_TO_5000 = "Enter a number between 1 and 5000."
}
