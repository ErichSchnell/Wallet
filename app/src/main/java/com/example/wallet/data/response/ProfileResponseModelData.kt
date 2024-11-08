package com.example.wallet.data.response

import androidx.compose.ui.graphics.Color
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.util.toStringHex

data class ProfileResponseModelData(
    val id: String? = null,
    val name: String? = null,
    val categorys: List<CategoryData?>? = null,
)

fun ProfileModelUi.toData() = ProfileResponseModelData(
    id = this.id,
    name = this.name,
    categorys = this.categories.map { it.toData() }
)

