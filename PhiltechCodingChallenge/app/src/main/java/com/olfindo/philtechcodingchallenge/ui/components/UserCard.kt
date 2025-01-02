package com.olfindo.philtechcodingchallenge.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.olfindo.philtechcodingchallenge.data.model.UserListResponse
import com.olfindo.philtechcodingchallenge.R
import com.olfindo.philtechcodingchallenge.data.model.CoordinatesResponse
import com.olfindo.philtechcodingchallenge.data.model.LocationResponse
import com.olfindo.philtechcodingchallenge.data.model.NameResponse
import com.olfindo.philtechcodingchallenge.data.model.PictureResponse
import com.olfindo.philtechcodingchallenge.data.model.StreetResponse
import com.olfindo.philtechcodingchallenge.data.model.TimezoneResponse

@Composable
fun UserCard(
    user: UserListResponse,
    onClick: (UserListResponse) -> Unit
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick(user) }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.picture.medium)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_person)
                    .build(),
                contentDescription = "Profile picture of ${user.name.first} ${user.name.last}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "${user.name.first} ${user.name.last}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${user.location.street.number} ${user.location.street.name}")
                Text(text = "${user.location.city}, ${user.location.state}")
                Text(text = "${user.location.country} ${user.location.postcode}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lat: ${user.location.coordinates.latitude}, Long: ${user.location.coordinates.longitude}"
                )
                Text(
                    text = "Timezone: ${user.location.timezone.offset} (${user.location.timezone.description})"
                )
            }
        }
    }
}


@Preview
@Composable
private fun UserCardPreview() {
    UserCard(
        user = UserListResponse(
            name = NameResponse("Mr.", "John", "Doe"),
            gender = "Male",
            email = "j.doe@sample.com",
            location = LocationResponse(
                street = StreetResponse(123, "Vicente St"),
                city = "Mandaluyong",
                state = "NCR",
                country = "Philippines",
                postcode = "12345",
                coordinates = CoordinatesResponse("14.5775°N", "121.0544°E"),
                timezone = TimezoneResponse("UTC+08:00", "Asia/Manila")
            ),
            picture = PictureResponse(
                "https://test.com/large.jpg",
                "https://test.com/medium.jpg",
                "https://test.com/thumbnail.jpg"
            )
        ),
        onClick = {}
    )
}
