package com.olfindo.philtechcodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.olfindo.philtechcodingchallenge.ui.navigation.NavGraph
import com.olfindo.philtechcodingchallenge.ui.theme.PhiltechCodingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point for the application.
 * This activity is responsible for setting up the UI and initializing the navigation graph.
 * It also enables edge-to-edge display to maximize the screen's usable area.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is created. Sets up the UI by enabling edge-to-edge display
     * and applying the app's theme to the navigation graph.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge display for a more immersive full-screen experience.
        enableEdgeToEdge()

        // Set up the content for the activity, applying the theme and navigation graph.
        setContent {
            PhiltechCodingChallengeTheme {
                // Navigation graph that manages all the navigation between different screens.
                NavGraph()
            }
        }
    }
}
