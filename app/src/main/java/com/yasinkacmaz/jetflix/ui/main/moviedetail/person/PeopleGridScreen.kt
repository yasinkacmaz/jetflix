package com.yasinkacmaz.jetflix.ui.main.moviedetail.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.widget.VerticalStaggeredGrid
import com.yasinkacmaz.jetflix.util.InsetsAmbient
import com.yasinkacmaz.jetflix.util.toDp

@Composable
fun PeopleGridScreen(people: List<Person>) {
    val insets = InsetsAmbient.current
    val statusBarPadding = insets.statusBars.top.toDp().dp
    val navigationBarPadding = insets.navigationBars.bottom.toDp().dp
    VerticalStaggeredGrid(
        itemCount = people.lastIndex,
        columnCount = 3,
        columnSpacing = 4.dp,
        rowSpacing = 4.dp,
        contentPadding = PaddingValues(start = 4.dp, end = 4.dp, top = statusBarPadding, bottom = navigationBarPadding),
        modifier = Modifier.background(MaterialTheme.colors.surface)
    ) { index, modifier ->
        val person = people[index]
        Person(
            profilePhotoUrl = person.profilePhotoUrl,
            name = person.name,
            job = person.character,
            gender = person.gender,
            modifier = modifier
        )
    }
}
