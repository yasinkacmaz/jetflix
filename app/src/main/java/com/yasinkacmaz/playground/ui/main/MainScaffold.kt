package com.yasinkacmaz.playground.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Normal
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.playground.R

@Composable
fun topAppBar() {
    TopAppBar(
        title = {
            Text(
                text = AnnotatedString("Playground"),
                style = TextStyle(
                    textAlign = Center,
                    fontStyle = Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        actions = {
            TopAppBarIcon(R.drawable.ic_movie)
            TopAppBarIcon(R.drawable.ic_search)
            TopAppBarIcon(R.drawable.ic_toys)
        },
        backgroundColor = Color.White
    )
}

@Composable
private fun TopAppBarIcon(@DrawableRes id: Int) {
    Icon(
        asset = vectorResource(id = id),
        modifier = Modifier.padding(start = 8.dp, end = 10.dp)
    )
}

@Composable
fun bottomNavigation() {
    BottomNavigation {
        BottomNavigationTab(R.drawable.ic_movie)
        BottomNavigationTab(R.drawable.ic_search)
        BottomNavigationTab(R.drawable.ic_toys)
    }
}

@Composable
private fun BottomNavigationTab(@DrawableRes id: Int) {
    BottomNavigationItem(
        icon = { Icon(asset = vectorResource(id = id)) },
        selected = false,
        onSelect = {}
    )
}
