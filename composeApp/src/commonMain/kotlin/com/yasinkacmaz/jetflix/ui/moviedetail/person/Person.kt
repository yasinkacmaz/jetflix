package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.placeholderIcon
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.JetflixImage
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.person_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun Person(person: Person, modifier: Modifier = Modifier) {
    val navController = LocalNavController.current
    Column(
        modifier.clickable { navController.navigate(Screen.Profile(person.id)) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JetflixImage(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            data = person.profilePhotoUrl,
            placeholder = rememberVectorPainter(person.gender.placeholderIcon),
            contentDescription = stringResource(Res.string.person_content_description, person.name, person.role),
            contentScale = ContentScale.FillWidth,
        )
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
