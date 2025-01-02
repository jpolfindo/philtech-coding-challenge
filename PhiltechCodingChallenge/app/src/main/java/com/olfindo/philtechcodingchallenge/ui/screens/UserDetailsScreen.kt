package com.olfindo.philtechcodingchallenge.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.olfindo.philtechcodingchallenge.R
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.ui.navigation.NavigationActions
import com.olfindo.philtechcodingchallenge.ui.theme.PhiltechCodingChallengeTheme
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UserViewModel

@Composable
fun UserDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    navigationActions: NavigationActions
) {

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        val selectedUser: UserListResponse? = viewModel.selectedUser.value

        Column(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp) // Reduced outer padding for better control over layout
        ) {
            // Show error or loading state if selectedUser is null
            selectedUser?.let {
                // Profile Picture and Info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.picture.large)
                                .crossfade(true)
                                .placeholder(R.drawable.ic_person)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(120.dp) // Increased size for better profile visibility
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${it.name.title} ${it.name.first} ${it.name.last}",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(text = it.gender, style = MaterialTheme.typography.bodyMedium)
                        Text(text = it.email, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // User Details Section
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(id = R.string.user_address),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(text = "${it.location.street.number} ${it.location.street.name}")
                        Text(text = "${it.location.city}, ${it.location.state}")
                        Text(text = "${it.location.country} ${it.location.postcode}")

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.user_coordinates),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(text = "${it.location.coordinates.latitude}, ${it.location.coordinates.longitude}")

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.user_timezone),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(text = "${it.location.timezone.offset} ${it.location.timezone.description}")
                    }
                }

            } ?: run {
                // Show error or empty state if user is null
                Text(
                    text = stringResource(id = R.string.no_user_selected),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Back Button
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { navigationActions.navigateBack() }, // Navigates back to the previous screen
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }
        }
    }

}

@Preview
@Composable
private fun UserDetailsScreenPreview() {

    val navController = rememberNavController()
    val navigationActions = remember { NavigationActions(navController) }

    PhiltechCodingChallengeTheme {
        UserDetailsScreen(
            navigationActions = navigationActions
        )
    }
}