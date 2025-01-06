package com.olfindo.philtechcodingchallenge.ui.navigation

import androidx.navigation.NavHostController
import com.olfindo.philtechcodingchallenge.ui.navigation.Screens.USER_DETAILS_SCREEN

/**
 * Object that defines the constants for the navigation routes used in the application.
 */
object Screens {
    /**
     * Route for the "Users" screen.
     */
    const val USERS_SCREEN = "users"

    /**
     * Route for the "User Details" screen.
     */
    const val USER_DETAILS_SCREEN = "userDetails"
}

/**
 * A utility class that encapsulates navigation actions for the app.
 *
 * The `NavigationActions` class provides a clean and reusable way to define and manage
 * navigation routes and actions, reducing boilerplate code in composables.
 *
 * @param navController The [NavHostController] used for navigation actions.
 */
class NavigationActions(private val navController: NavHostController) {

    /**
     * Navigates to the "User Details" screen.
     *
     * This function pushes the `USER_DETAILS_SCREEN` route onto the back stack, effectively
     * transitioning the app to the user details screen.
     */
    fun navigateToUserDetails() {
        navController.navigate(USER_DETAILS_SCREEN)
    }

    /**
     * Navigates back to the previous screen.
     *
     * This function pops the current screen from the back stack, returning the user to the
     * previous screen. If there are no screens left on the back stack, it closes the app or
     * the current activity.
     */
    fun navigateBack() {
        navController.popBackStack()
    }
}
