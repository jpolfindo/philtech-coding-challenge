# Philtech User Manager - Android Application

## Overview

This Android application retrieves a list of users from an API, allowing users to validate input, fetch user data, view user details, and handle various states like errors or loading. The project showcases modern Android development practices with a focus on robust ViewModel logic and unit testing.


## Features

### Core Features

- **User List**: Fetch and display a list of users with names and profile pictures.
- **Search User**: Search for a user by first name, last name, or email.
- **User Details**: View detailed user information, including full name, address, coordinates, profile picture, and timezone.
- **Input Validation**: Ensures user input falls within a specified range (1 to 5000).
- **Error Handling**: Gracefully handles network errors, general exceptions, and unknown issues with proper UI state updates.
- **State Reset**: Resets the UI state and selected user when required.

### Technical Features

1. Built using Jetpack Compose for a declarative and modern UI.
2. Dependency injection with Hilt.
3. Asynchronous operations with Kotlin Coroutines.
4. MockK for mocking dependencies in unit tests.
5. Unit tests to ensure ViewModel correctness.


## Dependencies

The following libraries and tools are used in this project:

- **Jetpack Compose**: Declarative UI toolkit.
- **Hilt**: Simplifies dependency injection.
- **Kotlin Coroutines**: For asynchronous programming.
- **Retrofit**: API client for HTTP requests.
- **OkHttp**: HTTP client for efficient network calls.
- **MockK**: For mocking during unit testing.
- **JUnit**: Test framework for unit testing.


## Architecture

The application follows the MVVM (Model-View-ViewModel) architecture:

1. **Model**: Contains the data source and business logic. API calls are handled by UserRepoManager.
2. **ViewModel**: Contains the logic for user input validation, fetching users, handling errors, and updating UI state.
3. **View**: Built with Jetpack Compose, it observes the UI state from the ViewModel and updates dynamically.


## Testing

Unit tests are provided for the `UserViewModel` to ensure its correctness. These include:

1. **Default State Validation**: Ensures the ViewModel initializes correctly.
2. **Input Validation**: Tests input validation logic.
3. **User Fetching**: Verifies interactions with the repository for fetching users.
4. **Error Handling**: Simulates various exceptions and validates UI state updates.
5. **State Reset**: Confirms proper resetting of the UI state.
5. **Duplicate Fetch Prevention**: Ensures redundant API calls are avoided.


## License

This project is licensed under the MIT License. See the LICENSE file for details.

