package com.olfindo.philtechcodingchallenge.data.source.remote

import com.olfindo.philtechcodingchallenge.data.model.RandomUserResponse
import javax.inject.Inject
import retrofit2.Response

/**
 * Data source class responsible for fetching data from the remote API.
 *
 * This class acts as an abstraction layer between the repository and the remote API,
 * delegating network calls to the [GetRandomUserService].
 *
 * @property randomUserService An instance of [GetRandomUserService] used to perform API requests.
 * @constructor Creates an instance of [RemoteDataSource] with the required dependencies injected.
 */
class RemoteDataSource @Inject constructor(
    private val randomUserService: GetRandomUserService
) {

    /**
     * Fetches a list of random users from the remote API.
     *
     * @param results The number of user results to fetch.
     * @return A [Response] wrapping [RandomUserResponse] containing the list of users.
     * @throws Exception If the API call fails or the response is invalid.
     *
     * Example usage:
     * ```
     * val response = remoteDataSource.getUsersFromNetwork(10)
     * if (response.isSuccessful) {
     *     val users = response.body()?.results
     * }
     * ```
     */
    suspend fun getUsersFromNetwork(results: Int): Response<RandomUserResponse> {
        return randomUserService.getUsers(results)
    }
}
