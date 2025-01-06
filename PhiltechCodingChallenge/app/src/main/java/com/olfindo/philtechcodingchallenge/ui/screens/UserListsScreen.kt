package com.olfindo.philtechcodingchallenge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.olfindo.philtechcodingchallenge.R
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.ui.components.UserCard

/**
 * A Composable function that displays a list of users with a search bar.
 * It allows filtering of the users by their names and emails based on the user's input in the search bar.
 *
 * @param modifier Modifier to be applied to the outer layout.
 * @param users List of users to display in the list.
 * @param onUserClick A callback function that is invoked when a user from the list is clicked.
 * @param onBackClick A callback function that is invoked when the back button is clicked.
 */
@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    users: List<UserListResponse>,
    onUserClick: (UserListResponse) -> Unit,
    onBackClick: () -> Unit
) {
    // State to store the search query
    var searchText by remember { mutableStateOf("") }

    // Filter the users list based on the search query
    val filteredUsers = if (searchText.isEmpty()) {
        users
    } else {
        users.filter { user ->
            "${user.name.first} ${user.name.last}".contains(searchText, ignoreCase = true) ||
                    user.email.contains(searchText, ignoreCase = true)
        }
    }

    // Scaffold layout for the screen, including top bar and content area
    Scaffold(
        topBar = {
            Column {
                // Custom Top Bar with Back Button and Title
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = stringResource(id = R.string.back_placeholder),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.fetch_users_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }

                // Search Bar for filtering users
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text(text = stringResource(id = R.string.search_user)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_placeholder)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true
                )
            }
        }
    ) { paddingValues ->
        // LazyColumn to display the list of filtered users
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // If no filtered users, show a message
            if (filteredUsers.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_user_found),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                // Display filtered users
                items(filteredUsers) { user ->
                    UserCard(user, onUserClick)
                }
            }
        }
    }
}
