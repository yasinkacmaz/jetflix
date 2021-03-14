package com.yasinkacmaz.jetflix.ui.main.moviedetail.person

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.toPlaceholderImageRes
import com.yasinkacmaz.jetflix.util.transformation.CircleTopCropTransformation
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Person(profilePhotoUrl: String?, name: String, job: String, gender: Gender, modifier: Modifier = Modifier) {
    Column(modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(shape = CircleShape, elevation = 8.dp, modifier = Modifier.size(120.dp)) {
            CoilImage(
                data = profilePhotoUrl ?: gender.toPlaceholderImageRes(),
                contentDescription = stringResource(id = R.string.person_photo_content_description, name, job),
                fadeIn = true,
                requestBuilder = { transformations(CircleTopCropTransformation()) },
                contentScale = ContentScale.FillHeight
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = job,
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
