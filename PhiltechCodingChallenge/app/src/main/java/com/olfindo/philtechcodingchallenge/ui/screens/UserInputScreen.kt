package com.olfindo.philtechcodingchallenge.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.olfindo.philtechcodingchallenge.R
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManager
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UserViewModel
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UsersUiState
import com.olfindo.philtechcodingchallenge.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Composable function to display a user input screen where the user can enter the number of results
 * to generate a user list.
 *
 * The screen consists of a number input field and a button to trigger the user list generation.
 * It also handles the input validation, showing an error message if the input is invalid or exceeds a limit.
 *
 * @param modifier Modifier to be applied to the outer layout.
 * @param viewModel The [UserViewModel] that holds the user data and manages the state.
 * @param onUserClick Callback function to be invoked when a user is selected.
 */
@Composable
fun UserInputScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    onUserClick: () -> Unit
) {

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->

        // Collecting the UI state from the view model
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val invalidInputError = stringResource(id = R.string.invalid_input_error)

        // State variables for managing input text and error messages
        var inputText by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        // BackHandler to reset UI state on back press
        BackHandler {
            viewModel.resetUIState()
        }

        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show a loading indicator if data is being fetched
            if (uiState.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Show the user list or input screen based on the state
            if (uiState.value.users.isNotEmpty()) {
                UsersList(
                    modifier = modifier,
                    users = uiState.value.users,
                    onUserClick = { user ->
                        viewModel.selectUser(user)
                        onUserClick() // Proceed with user selection
                    },
                    onBackClick = {
                        viewModel.resetUIState() // Reset state and show input screen
                    }
                )
            } else if (uiState.value.showInput && uiState.value.users.isEmpty()) {
                // Display the input UI for entering number of results
                Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp) // Adds horizontal padding
                ) {
                    // Title and instruction for input
                    Text(
                        text = stringResource(id = R.string.fetch_users_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.enter_number_instruction),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Outlined text field for input
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { newText ->
                            val filteredText = newText.filter { it.isDigit() }
                            inputText = filteredText

                            // Validate the input and show appropriate error message
                            errorMessage = if (filteredText.isNotEmpty()) {
                                val inputValue = filteredText.toIntOrNull()
                                when (inputValue) {
                                    null -> invalidInputError
                                    in Constants.RANGE -> ""
                                    else -> invalidInputError
                                }
                            } else {
                                "" // Clear error message if input is empty
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.enter_number_of_results_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = errorMessage.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)) // Rounded corners for input field
                    )

                    // Display the error message if the input is invalid
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to trigger the generation of user list
                    Button(
                        onClick = {
                            viewModel.validateInput(inputText.toIntOrNull() ?: 0)
                        },
                        enabled = inputText.isNotBlank() && errorMessage.isEmpty(),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.generate_user_list_label))
                    }
                }
            }
        }
    }
}

/**
 * Preview of the UserInputScreen composable.
 *
 * This preview displays a sample user input screen with mock data for visual testing.
 */
@Preview(showBackground = true)
@Composable
fun UserInputScreenPreview() {
    // Create a mock viewModel
    val mockViewModel = object : UserViewModel(userRepository = FakeUserRepo()) {
        override val uiState: StateFlow<UsersUiState> = MutableStateFlow(
            UsersUiState(
                isLoading = false,
                users = emptyList(),
                showInput = true
            )
        )

        // Override resetUIState and other methods if necessary for preview
        override fun resetUIState() {
            // Mock reset functionality (do nothing)
        }

        override fun fetchUsers(count: Int) {
            // Mock fetch behavior for preview (no actual fetching)
        }

        override fun selectUser(user: UserListResponse) {
            // Mock select user functionality (do nothing)
        }

        override fun validateInput(count: Int) {
            // Mock validation (if necessary for preview)
        }
    }

    // Provide an empty lambda for onUserClick (this won't affect the preview)
    UserInputScreen(viewModel = mockViewModel, onUserClick = {})
}

// Fake UserRepo for preview (just returns an empty list)
class FakeUserRepo : UserRepoManager {
    override suspend fun getUsers(results: Int): List<UserListResponse> {
        // Mock the response
        return emptyList() // You can return sample data if needed
    }
}
