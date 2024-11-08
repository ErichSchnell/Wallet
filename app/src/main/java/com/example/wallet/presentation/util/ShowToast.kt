package com.example.wallet.presentation.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToast(text: String, clearToast:() -> Unit) {
    val context = LocalContext.current
    if (text.isNotEmpty()){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        clearToast()
    }
}