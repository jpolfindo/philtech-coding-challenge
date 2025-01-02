package com.olfindo.philtechcodingchallenge.ui.viewmodels

import android.accounts.NetworkErrorException
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState

    private val _selectedUser = mutableStateOf<UserListResponse?>(null)
    val selectedUser: State<UserListResponse?> = _selectedUser

    private var hasFetchedUsers = false

    fun fetchUsers(count: Int) {
        if (!hasFetchedUsers) {
            updateUIState(isLoading = true, showInput = false)

            viewModelScope.launch {
                try {
                    val result = userRepository.getUsers(count)
                    updateUIState(users = result, isLoading = false)
                    hasFetchedUsers = true
                } catch (e: NetworkErrorException) {
                    handleError(e, ErrorType.NETWORK)
                } catch (e: Exception) {
                    handleError(e, ErrorType.UNKNOWN)
                }
            }
        }
    }

    fun selectUser(user: UserListResponse) {
        _selectedUser.value = user
    }

    fun validateInput(count: Int) {
        if (count in 1..5000) {
            updateUIState(errorMessage = null)
            fetchUsers(count)
        } else {
            updateUIState(errorMessage = ErrorMessages.INPUT_1_TO_5000)
        }
    }

    fun resetUIState() {
        _uiState.value = UsersUiState()
        _selectedUser.value = null
        hasFetchedUsers = false
    }

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

    private fun handleError(exception: Exception, errorType: ErrorType) {
        val errorMessage = when (errorType) {
            ErrorType.NETWORK -> ErrorMessages.NETWORK_ERROR
            ErrorType.UNKNOWN -> exception.message ?: ErrorMessages.UNKNOWN_ERROR
        }
        updateUIState(isLoading = false, showInput = true, errorMessage = errorMessage)
    }

    companion object {
        private enum class ErrorType {
            NETWORK, UNKNOWN
        }
    }
}

data class UsersUiState(
    val users: List<UserListResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showInput: Boolean = true
)

object ErrorMessages {
    const val NETWORK_ERROR = "Network error"
    const val UNKNOWN_ERROR = "Unknown error"
    const val INPUT_1_TO_5000 = "Enter a number between 1 and 5000"
}

