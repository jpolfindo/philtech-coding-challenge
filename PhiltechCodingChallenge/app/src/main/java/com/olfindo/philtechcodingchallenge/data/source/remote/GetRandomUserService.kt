package com.olfindo.philtechcodingchallenge.data.source.remote

import com.olfindo.philtechcodingchallenge.data.model.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service interface for interacting with the Random User API.
 *
 * This interface defines methods for fetching random user data from the remote API.
 */
interface GetRandomUserService {

    /**
     * Fetches a list of random users from the API.
     *
     * @param results The number of user results to fetch.
     * @return A [Response] wrapping [RandomUserResponse] containing the list of users.
     * @throws Exception If the API call fails or the response cannot be processed.
     *
     * Example usage:
     * ```
     * val response = service.getUsers(10)
     * if (response.isSuccessful) {
     *     val users = response.body()?.results
     * }
     * ```
     */
    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int): Response<RandomUserResponse>
}
