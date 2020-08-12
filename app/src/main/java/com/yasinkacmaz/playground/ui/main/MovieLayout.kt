package com.yasinkacmaz.playground.ui.main

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.playground.data.Movie

@Composable
fun movieCard(movie: Movie) {
    Column(horizontalGravity = Alignment.CenterHorizontally) {
        MovieRate(movie.voteAverage)
        Card(shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
            Column() {
                Text(text = AnnotatedString(movie.name))
            }
        }
    }
}

@Composable
fun MovieRate(rate: Double) {
    Box(
        backgroundColor = Color(255, 177, 10),
        shape = RoundedCornerShape(12.dp),
        gravity = ContentGravity.Center
    ) {
        Text(
            text = rate.toString(),
            style = TextStyle(fontSize = 12.sp),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}