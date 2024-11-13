package com.example.wallet.presentation.model

import androidx.compose.ui.graphics.Color
import com.example.wallet.data.response.CategoryData
import com.example.wallet.presentation.util.ex.toColor
import java.util.Date


data class CategoryUi(
    val id: String = "",
    val name:String = "",
    val amount:Double = .0,
    val color: Color = Color(0xFFFFFFFF)
)

fun CategoryData.toUi() = CategoryUi(
    id = id,
    name = name,
    color = color.toColor(),
)

fun CategoryUi.withNewId(): CategoryUi {
    return this.copy(id = Date().time.toString())
}