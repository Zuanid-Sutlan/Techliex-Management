package com.techliexai.management.presetation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.techliexai.management.R

@Composable
fun Loading(showLoading: Boolean = false) {
    AnimatedVisibility(showLoading, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {}
                .background(Color(0x80FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading2))
            LottieAnimation(
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp),
                composition = composition.value,
                isPlaying = true,
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}