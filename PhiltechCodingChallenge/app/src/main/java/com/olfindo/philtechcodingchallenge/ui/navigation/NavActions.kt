package com.olfindo.philtechcodingchallenge.ui.navigation

import androidx.navigation.NavHostController
import com.olfindo.philtechcodingchallenge.ui.navigation.Screens.USER_DETAILS_SCREEN

object Screens {
    const val USERS_SCREEN = "users"
    const val USER_DETAILS_SCREEN = "userDetails"
}

class NavigationActions(private val navController: NavHostController) {

    fun navigateToUserDetails() {
        navController.navigate(USER_DETAILS_SCREEN)
    }

    fun navigateBack() {
        navController.popBackStack() // This will pop the current screen and go back
    }

}