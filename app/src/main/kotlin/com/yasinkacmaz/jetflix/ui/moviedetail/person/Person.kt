package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.placeholderIcon
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.transformation.CircleTopCropTransformation

@Composable
fun Person(person: Person, modifier: Modifier = Modifier) {
    val navController = LocalNavController.current
    Column(
        modifier.clickable { navController.navigate(Screen.Profile(person.id)) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(shape = CircleShape, modifier = Modifier.size(120.dp)) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(person.profilePhotoUrl)
                .crossfade(true)
                .transformations(CircleTopCropTransformation())
                .build()
            val placeholderPainter = rememberVectorPainter(person.gender.placeholderIcon)
            val painter =
                rememberAsyncImagePainter(model = request, error = placeholderPainter, placeholder = placeholderPainter)
            val colorFilter = when (painter.state) {
                is Error, is Loading -> ColorFilter.tint(Color.LightGray)
                else -> null
            }
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                colorFilter = colorFilter,
                contentDescription = stringResource(id = R.string.person_content_description, person.name, person.role),
                contentScale = ContentScale.FillWidth,
            )
        }
        Text(
            text = person.name,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
        )
        Text(
            text = person.role,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = MaterialTheme.spacing.xxs),
        )
    }
}
