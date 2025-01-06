package com.olfindo.philtechcodingchallenge.data.repository

import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.data.source.remote.RemoteDataSource
import javax.inject.Inject

/**
 * Implementation of the [UserRepoManager] interface for managing user-related data operations.
 *
 * This class fetches user data from a remote data source and handles error scenarios.
 *
 * @property remoteDataSource The data source responsible for fetching data from the network.
 * @constructor Injects the required dependencies into this class using Dagger.
 */
class UserRepoManagerImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepoManager {

    /**
     * Fetches a list of random users from the remote data source.
     *
     * @param results The number of user results to fetch.
     * @return A list of [UserListResponse] containing user data fetched from the network.
     * @throws Exception If the network call fails or the response is unsuccessful.
     */
    override suspend fun getUsers(results: Int): List<UserListResponse> {
        return try {
            // Attempt to fetch users from the network
            val response = remoteDataSource.getUsersFromNetwork(results)

            if (response.isSuccessful) {
                val users = response.body()?.results ?: emptyList()

                // Return the network results
                users
            } else {
                throw Exception("Error querying list of users: ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch users from remote sources.", e)
        }
    }
}
