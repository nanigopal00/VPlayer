package com.example.vplayer.UI_layer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vplayer.R
import com.example.vplayer.ui.theme.myTealColor
import kotlinx.coroutines.delay

@Composable
fun flashscreen(navcontroll: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.5f else 0.5f,
        animationSpec = tween(
            durationMillis = 1500, // Animation duration: 1 second
            delayMillis = 0
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true // Trigger the animation
        delay(1500) // Wait for 2 seconds (animation + extra delay)
        navcontroll.navigate(homescreen) {
            popUpTo(0) { // Pop up to the start destination (index 0)
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.video), // Replace with your icon
            contentDescription = "Splash Screen Icon", // Add a content description
            tint = myTealColor,
            modifier = Modifier
                .size(130.dp)
                .scale(scale) // Apply the animated scale
        )
    }
}