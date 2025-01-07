package com.olfindo.philtechcodingchallenge.data.model


/**
 * Represents the response from the Random User API.
 *
 * @property results A list of user data returned by the API.
 */
data class RandomUserResponse(
    val results: List<UserListResponse>
)

/**
 * Represents details about a user.
 *
 * @property name The user's name details.
 * @property gender The user's gender (e.g., "male", "female").
 * @property location The user's location details.
 * @property email The user's email address.
 * @property picture The user's profile picture details.
 */
data class UserListResponse(
    val name: NameResponse,
    val gender: String,
    val location: LocationResponse,
    val email: String,
    val picture: PictureResponse,
    val cell: String,
    val phone: String,
    val nat: String,
    val dob: BirthDateResponse
)

/**
 * Represents the name details of a user.
 *
 * @property title The user's title (e.g., "Mr", "Ms").
 * @property first The user's first name.
 * @property last The user's last name.
 */
data class NameResponse(
    val title: String,
    val first: String,
    val last: String
)

/**
 * Represents the birth details of a user.
 *
 * @property date The user's date of birth (e.g., 1949-03-20T01:28:14.666Z).
 * @property age The user's age.
 */
data class BirthDateResponse(
    val date: String,
    val age: Int
)

/**
 * Represents the location details of a user.
 *
 * @property street The user's street address details.
 * @property city The user's city.
 * @property state The user's state.
 * @property country The user's country.
 * @property postcode The user's postal code.
 * @property coordinates The geographic coordinates of the user's location.
 * @property timezone The timezone details of the user's location.
 */
data class LocationResponse(
    val street: StreetResponse,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: CoordinatesResponse,
    val timezone: TimezoneResponse
)

/**
 * Represents the street details of a user's location.
 *
 * @property number The street number.
 * @property name The street name.
 */
data class StreetResponse(
    val number: Int,
    val name: String
)

/**
 * Represents geographic coordinates of a user's location.
 *
 * @property latitude The latitude value.
 * @property longitude The longitude value.
 */
data class CoordinatesResponse(
    val latitude: String,
    val longitude: String
)

/**
 * Represents timezone details of a user's location.
 *
 * @property offset The timezone offset (e.g., "+02:00").
 * @property description The timezone description (e.g., "Central European Time").
 */
data class TimezoneResponse(
    val offset: String,
    val description: String
)

/**
 * Represents details about a user's profile picture.
 *
 * @property large The URL of the large version of the profile picture.
 * @property medium The URL of the medium version of the profile picture.
 * @property thumbnail The URL of the thumbnail version of the profile picture.
 */
data class PictureResponse(
    val large: String,
    val medium: String,
    val thumbnail: String
)

/**
 * Provides a default instance of [UserListResponse] in case the original instance is null.
 *
 * @receiver The nullable instance of [UserListResponse].
 * @return A default [UserListResponse] instance with placeholder data.
 */
fun UserListResponse?.orDefault(): UserListResponse {
    return this ?: UserListResponse(
        name = NameResponse("Mr/Ms", "Unknown", "User"),
        gender = "Unknown",
        location = LocationResponse(
            street = StreetResponse(0, "Unknown Street"),
            city = "Unknown City",
            state = "Unknown State",
            country = "Unknown Country",
            postcode = "00000",
            coordinates = CoordinatesResponse("0.0", "0.0"),
            timezone = TimezoneResponse("+0:00", "UTC")
        ),
        email = "No Email",
        picture = PictureResponse(
            large = "https://example.com/default_large.jpg",
            medium = "https://example.com/default_medium.jpg",
            thumbnail = "https://example.com/default_thumbnail.jpg"
        ),
        cell = "Unknown Mobile Number",
        phone = "Unknown Phone Number",
        nat = "Unknown Nationality",
        dob = BirthDateResponse(
            date = "Unknown Birthdate",
            age = 0
        )
    )
}
