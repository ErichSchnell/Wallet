package com.example.wallet.presentation.util

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

fun String.toLong(): Long? {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
    return try {
        dateFormat.parse(this)?.time
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}