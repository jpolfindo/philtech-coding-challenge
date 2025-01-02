package com.olfindo.philtechcodingchallenge.data.source.remote

import com.olfindo.philtechcodingchallenge.data.model.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRandomUserService {

    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int): Response<RandomUserResponse>

}