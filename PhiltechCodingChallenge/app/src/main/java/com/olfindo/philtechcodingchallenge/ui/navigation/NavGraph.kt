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

/**
 * Composable function that defines the navigation graph of the app.
 *
 * This function creates and manages the navigation host, defining the navigation routes
 * and the corresponding screens that should be displayed. The navigation graph is constructed
 * using the [NavHost] composable and allows users to navigate between different screens
 * like the user input screen and user details screen.
 *
 * @param modifier Modifier to be applied to the NavHost composable.
 * @param navController The [NavHostController] responsible for managing the navigation stack.
 * @param startDestination The screen to display first when the app starts.
 * @param sharedUsersViewModel The [UserViewModel] shared across the screens to manage user data.
 * @param navActions Navigation actions to handle screen transitions and back stack operations.
 */
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
    // Set up the navigation graph with defined destinations
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
