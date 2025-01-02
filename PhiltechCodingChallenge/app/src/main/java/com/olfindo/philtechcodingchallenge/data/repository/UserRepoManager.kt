package com.olfindo.philtechcodingchallenge.data.repository

import com.olfindo.philtechcodingchallenge.data.model.UserListResponse

interface UserRepoManager {

    suspend fun getUsers(results: Int): List<UserListResponse>

}