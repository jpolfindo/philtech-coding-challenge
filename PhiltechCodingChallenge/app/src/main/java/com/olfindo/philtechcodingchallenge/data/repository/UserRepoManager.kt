package com.olfindo.philtechcodingchallenge.data.repository

import com.olfindo.philtechcodingchallenge.data.model.UserListResponse

/**
 * Interface for managing user-related data operations.
 *
 * This interface defines the contract for fetching user data from a remote or local data source.
 */
interface UserRepoManager {

    /**
     * Fetches a list of random users.
     *
     * @param results The number of user results to fetch.
     * @return A list of [UserListResponse] containing user data.
     * @throws Exception Throws an exception if the operation fails, such as network or parsing errors.
     */
    suspend fun getUsers(results: Int): List<UserListResponse>
}
