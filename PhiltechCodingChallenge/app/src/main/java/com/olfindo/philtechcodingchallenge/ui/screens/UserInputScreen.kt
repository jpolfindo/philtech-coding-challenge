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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.olfindo.philtechcodingchallenge.R
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UserViewModel

@Composable
fun UserInputScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    onUserClick: () -> Unit
) {

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->

        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val invalidInputError = stringResource(id = R.string.invalid_input_error)

        var inputText by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        BackHandler {
            viewModel.resetUIState()
        }

        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (uiState.value.users.isEmpty().not()) {
                UsersList(
                    modifier = modifier,
                    users = uiState.value.users,
                    onUserClick = { user ->
                        viewModel.selectUser(user)
                        onUserClick() // Proceed with the user selection
                    },
                    onBackClick = {
                        viewModel.resetUIState() // Reset state to input screen
                    }
                )
            } else if (uiState.value.showInput && uiState.value.users.isEmpty()) {
                Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp) // Adds horizontal padding
                ) {
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

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { newText ->
                            val filteredText = newText.filter { it.isDigit() }
                            inputText = filteredText

                            // Check if the input value exceeds 5000 or is invalid
                            errorMessage = if (filteredText.isNotEmpty()) {
                                val inputValue = filteredText.toIntOrNull()
                                when (inputValue) {
                                    null -> {
                                        invalidInputError
                                    }
                                    in 1..5000 -> {
                                        ""
                                    }
                                    else -> {
                                        invalidInputError
                                    }
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
                            .clip(RoundedCornerShape(8.dp)) // Rounded corners
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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