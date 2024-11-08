package com.example.wallet.presentation.model

import com.example.wallet.R
import com.example.wallet.data.response.TransactionResponseModelData
import com.example.wallet.presentation.util.TypeOfTransaction
import com.example.wallet.presentation.util.TypeOfTransaction.*
import com.example.wallet.ui.theme.WalletColors

data class TransactionModelUI(
    val id: String = "",
    val description: String = "",
    val amount: Double = .0,
    val category: CategoryUi = CategoryUi(),
    val budget: TypeOfTransaction = EMPTY,
    val icon: Int = R.drawable.ic_wallet_circle_outline,
    val colorBudget: WalletColors = WalletColors(),
    val isBookmark:Boolean = false,
    val date: Long? = null,
)

fun TransactionResponseModelData.toUi(): TransactionModelUI? {
    if (this.id == null) return null
    if (this.motive == null) return null
    if (this.amount == null) return null
    if (this.category == null) return null
    if (this.budget == null) return null
    if (this.date == null) return null

    val date = this.date.seconds * 1000 + this.date.nanoseconds / 1000000
    val amount: Double
    val icon: Int
    val color: WalletColors

    if (TypeOfTransaction.getType(this.budget) == EXPENSES) {
        amount = this.amount * -1
        icon = R.drawable.ic_cash_minus
        color = WalletColors.expenses()
    } else {
        amount = this.amount
        icon = R.drawable.ic_cash_plus
        color = WalletColors.icome()
    }

    return TransactionModelUI(
        id = this.id,
        description = this.motive,
        icon = icon,
        colorBudget = color,
        amount = amount,
        category = CategoryUi( id = this.category),
        budget = TypeOfTransaction.getType(this.budget),
        isBookmark = this.isBookmark ?: false,
        date = date,
    )
}