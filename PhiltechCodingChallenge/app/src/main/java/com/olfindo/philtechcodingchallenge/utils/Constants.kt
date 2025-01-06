package com.olfindo.philtechcodingchallenge.utils

/**
 * A utility class that holds constant values used throughout the application.
 * These constants provide configuration values that are used for network requests,
 * as well as the valid range of user input.
 */
class Constants {

    companion object {

        /**
         * Timeout duration for HTTP requests.
         * The default timeout is set to 30 seconds.
         *
         * @property REQUEST_TIMEOUT The timeout duration in seconds.
         */
        const val REQUEST_TIMEOUT: Long = 30L

        /**
         * A valid range for user input. This range is used to validate user input
         * when determining how many users to fetch (between 1 and 5000).
         *
         * @property RANGE The valid range for the number of users.
         */
        val RANGE = 1..5000
    }
}
