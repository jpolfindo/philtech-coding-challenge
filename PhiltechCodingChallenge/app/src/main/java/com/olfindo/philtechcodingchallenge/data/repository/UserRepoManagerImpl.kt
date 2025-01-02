package com.olfindo.philtechcodingchallenge.data.repository

import com.olfindo.philtechcodingchallenge.data.source.remote.GetRandomUserService
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import javax.inject.Inject

class UserRepoManagerImpl @Inject constructor(private val service: GetRandomUserService) : UserRepoManager {

    override suspend fun getUsers(results: Int): List<UserListResponse> {
        val response = service.getUsers(results)

        return if (response.isSuccessful) {
            response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error querying list of users")
        }

    }

}