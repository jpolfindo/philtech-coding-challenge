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

1. **Default State Validation**: Ensures the ViewModel initializes correctly and shows input by default.
2. **Input Validation**: Verifies that user input is correctly validated within the range of 1 to 5000, with error messages shown for invalid inputs.
3. **User Fetching**: Confirms that valid inputs trigger API calls to fetch users and update the UI state with the results.
4. **Selected User Update**: Ensures that selecting a user correctly updates the selected user in the ViewModel.
5. **Duplicate Fetch Prevention**: Verifies that the repository is called only once, even if multiple fetch requests are made for the same data.
6. **UI State Reset**: Confirms that the UI state is properly reset, clearing the users and restoring the initial state.
7. **Error Handling**: Simulates different errors (network, general, and unknown) during data fetching and ensures the UI state is updated with appropriate error messages.
8. **Loading State**: Verifies that the loading state behaves correctly during data fetching, showing as true during the fetch and false once it completes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

