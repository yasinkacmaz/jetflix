package com.yasinkacmaz.playground.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Normal
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Center
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.playground.R

@Composable
fun TopAppBar(initialTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
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
            ChangeTheme(initialTheme, onThemeChanged)
        }
    )
}

@Composable
private fun ChangeTheme(initialTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    Text(text = stringResource(id = R.string.change_theme))
    Switch(checked = initialTheme, onCheckedChange = onThemeChanged)
}

@Composable
fun BottomNavigation() {
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
