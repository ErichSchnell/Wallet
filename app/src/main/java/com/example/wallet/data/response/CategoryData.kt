package com.example.wallet.data.response

import androidx.compose.ui.graphics.Color
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.util.toStringHex
import java.util.Date

data class CategoryData(
    val id: String = "",
    val name:String = "",
    val color: String = ""
)

fun CategoryUi.toData(): CategoryData {
    return CategoryData(
        id = id,
        name = name,
        color = color.toStringHex(),
    )
}
