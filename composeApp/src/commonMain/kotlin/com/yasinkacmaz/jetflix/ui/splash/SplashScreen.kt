package com.yasinkacmaz.jetflix.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.ic_splash
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

private const val SPLASH_DURATION_MS = 500L

@Composable
fun SplashScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.size(160.dp),
                painter = painterResource(Res.drawable.ic_splash),
                contentDescription = null,
            )
        }
    }

    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        navController.navigate(Screen.Movies) {
            popUpTo(Screen.Splash) { inclusive = true }
        }
    }
}
