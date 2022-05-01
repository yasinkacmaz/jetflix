package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.toPlaceholderImageRes
import com.yasinkacmaz.jetflix.util.transformation.CircleTopCropTransformation

@Composable
fun Person(person: Person, modifier: Modifier = Modifier) {
    Column(modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(shape = CircleShape, elevation = 8.dp, modifier = Modifier.size(120.dp)) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(data = person.profilePhotoUrl ?: person.gender.toPlaceholderImageRes())
                .crossfade(true)
                .transformations(CircleTopCropTransformation())
                .placeholder(person.gender.toPlaceholderImageRes())
                .build()
            AsyncImage(
                model = request,
                contentDescription = stringResource(id = R.string.person_content_description, person.name, person.role),
                contentScale = ContentScale.FillHeight
            )
        }
        Text(
            text = person.name,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = person.role,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
