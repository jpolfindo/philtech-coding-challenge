package com.olfindo.philtechcodingchallenge.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

class DateFormatter {

    // Utility function to format the date
    fun formatDate(dateString: String): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Use java.time for API level 26 and above
                val zonedDateTime = ZonedDateTime.parse(dateString) // Parse the ISO 8601 date
                val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault()) // Desired format
                zonedDateTime.format(formatter)
            } else {
                // Use SimpleDateFormat for API levels below 26
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure UTC timezone
                val date = simpleDateFormat.parse(dateString) // Parse the date string
                val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) // Desired format
                outputFormat.format(date) // Format the date
            }
        } catch (e: Exception) {
            "Invalid date" // Fallback if the date string cannot be parsed
        }
    }
}