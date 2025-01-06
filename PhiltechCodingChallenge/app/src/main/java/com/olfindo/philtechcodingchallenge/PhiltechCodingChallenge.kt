package com.olfindo.philtechcodingchallenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class for the Philtech Coding Challenge application.
 * This class is used to initialize Dagger Hilt for dependency injection across the app.
 *
 * @HiltAndroidApp annotation triggers Hilt's code generation for the application,
 * allowing it to manage dependency injection and providing the required
 * application context for injecting dependencies.
 */
@HiltAndroidApp
class PhiltechCodingChallenge : Application()