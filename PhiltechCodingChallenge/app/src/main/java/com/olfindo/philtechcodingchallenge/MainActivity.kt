package com.olfindo.philtechcodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.olfindo.philtechcodingchallenge.ui.navigation.NavGraph
import com.olfindo.philtechcodingchallenge.ui.theme.PhiltechCodingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhiltechCodingChallengeTheme {
                NavGraph()
            }
        }
    }
}