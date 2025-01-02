package com.olfindo.philtechcodingchallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olfindo.philtechcodingchallenge.ui.screens.UserDetailsScreen
import com.olfindo.philtechcodingchallenge.ui.screens.UserInputScreen
import com.olfindo.philtechcodingchallenge.ui.viewmodels.UserViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.USERS_SCREEN,
    sharedUsersViewModel: UserViewModel = hiltViewModel(),
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // Users input and list screen
        composable(Screens.USERS_SCREEN) {
            UserInputScreen(
                viewModel = sharedUsersViewModel,
                onUserClick = {
                    // Navigate to the user details screen when a user is clicked
                    navActions.navigateToUserDetails()
                }
            )
        }

        // User details screen
        composable(Screens.USER_DETAILS_SCREEN) {
            UserDetailsScreen(
                viewModel = sharedUsersViewModel,
                navigationActions = navActions // Pass navActions to the user details screen as well
            )
        }

    }
}