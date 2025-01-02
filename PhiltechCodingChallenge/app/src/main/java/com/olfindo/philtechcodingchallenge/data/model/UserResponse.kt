package com.olfindo.philtechcodingchallenge.data.model;


data class RandomUserResponse(
    val results: List<UserListResponse>
)

data class UserListResponse(
    val name: NameResponse,
    val gender: String,
    val location: LocationResponse,
    val email: String,
    val picture: PictureResponse
)

data class NameResponse(
        val title: String,
        val first: String,
        val last: String
)

data class LocationResponse(
    val street: StreetResponse,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: CoordinatesResponse,
    val timezone: TimezoneResponse
)

data class StreetResponse(
        val number: Int,
        val name: String,
        )

data class CoordinatesResponse(
        val latitude: String,
        val longitude: String,
        )

data class TimezoneResponse(
        val offset: String,
        val description: String,
        )

data class PictureResponse(
        val large: String,
        val medium: String,
        val thumbnail: String,
        )
