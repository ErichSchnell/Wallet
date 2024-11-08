package com.example.wallet.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CardWallet(
    content: @Composable () -> Unit
) {
    Card (
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 16.dp, vertical = 8.dp),
        shape =  RoundedCornerShape(12.dp),
        colors =  CardDefaults.cardColors().copy(containerColor = Color.Black.copy(alpha = .8f)),
    ) {
        content()
    }
}

@Composable
fun CardWallet(
    height: Dp,
    content: @Composable () -> Unit
) {
    Card (
        modifier = Modifier.fillMaxWidth().height(height).padding(horizontal = 16.dp, vertical = 8.dp),
        shape =  RoundedCornerShape(12.dp),
        colors =  CardDefaults.cardColors().copy(containerColor = Color.Black.copy(alpha = .8f)),
    ) {
        content()
    }
}