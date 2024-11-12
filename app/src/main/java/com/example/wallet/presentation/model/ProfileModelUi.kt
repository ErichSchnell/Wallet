package com.example.wallet.presentation.model

import android.util.Log
import com.example.wallet.data.response.ProfileResponseModelData
import com.example.wallet.presentation.util.toColor
import com.example.wallet.presentation.util.toStringHex
import com.example.wallet.ui.theme.OnHomeContainer
import java.util.Date

data class ProfileModelUi(
    val id: String = Date().time.toString(),
    val name: String = "perfil_1",
    val categories: List<CategoryUi> = emptyList()
)

fun ProfileResponseModelData.toUi(): ProfileModelUi {
    return ProfileModelUi(
        id = id.orEmpty(),
        name = name.orEmpty(),
        categories = this.categorys?.mapNotNull { it?.toUi() } ?: emptyList(),
    )
}
