package com.example.wallet.presentation.model

import com.example.wallet.data.response.UserResponseModelData

data class UserModelUI(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
)

fun UserResponseModelData.toUi() = UserModelUI(
    firstName = firstname.orEmpty(),
    lastName = lastname.orEmpty(),
    email = email.orEmpty(),
)
