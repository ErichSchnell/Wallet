package com.example.wallet.presentation.util.ex

import androidx.compose.ui.graphics.Color
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.ui.theme.OnHomeContainer


fun List<TransactionModelUI>.setWithCategory(categories: List<CategoryUi>?): List<TransactionModelUI>{
    categories ?: return this

    return this.map { tr ->
        val ct = categories.find { it.id == tr.category.id }
        if (ct != null) {
            tr.copy(category = ct)
        } else {
            tr.copy(category = CategoryUi(name = "error", color = Color.Red))//
        }
    }
}

fun List<TransactionModelUI>.getCategories(categories: List<CategoryUi>?): List<CategoryUi>{
    categories ?: return emptyList()

    val categoriesTitles = mutableListOf("Total")
    categoriesTitles.addAll(this.map { it.category.id }.distinct())

    var index = 0

    return categoriesTitles.map { categoryId ->
        val ct = categories.find { it.id == categoryId }

        val cate = if (ct != null){
            ct.copy(amount = this.sumOf { if (it.category.id == categoryId) it.amount else .0 })
        } else {
            CategoryUi(
                id = if (index == 0) "Total" else "",
                name = if (index == 0) "Total" else "",
                amount = if (index == 0) this.sumOf { it.amount } else this.sumOf { if (it.category.id == categoryId) it.amount else .0 },
                color = OnHomeContainer
            )
        }
        index++

        cate
    }
}

fun List<TransactionModelUI>.mapWithDate(date: String?): List<TransactionModelUI>{
    date ?: return this
    return this.mapNotNull { tr ->
        if (tr.date?.getMonthAndYearString() == date) tr
        else null
    }
}

fun List<TransactionModelUI>.mapWithCategory(categorySelected: CategoryUi): List<TransactionModelUI>{
    if (categorySelected.id == "Total" || categorySelected.id == "") return this
    val tr = this.mapNotNull {
        if (it.category.id == categorySelected.id) it else null
    }
    return tr
}


fun List<TransactionModelUI>.getWeek(numberOfWeek: Int): List<TransactionModelUI> {
    return when(numberOfWeek){
        1 -> {
            this.mapNotNull { transaction ->
                val date = transaction.date.getDayOfMonth() ?: 8
                if (date <= 7) transaction else null
            }
        }
        2 -> {
            this.mapNotNull { transaction ->
                val date = transaction.date.getDayOfMonth() ?: 0
                if (date > 7 && date <= 14) transaction else null
            }
        }
        3 -> {
            this.mapNotNull { transaction ->
                val date = transaction.date.getDayOfMonth() ?: 0
                if (date > 14 && date <= 21) transaction else null
            }
        }
        else -> {
            this.mapNotNull { transaction ->
                val date = transaction.date.getDayOfMonth() ?: 0
                if (date > 21 && date <= 31) transaction else null
            }
        }
    }
}


fun List<TransactionModelUI>.getDay(numberOfWeek: Int): List<TransactionModelUI> {
    return this.mapNotNull { tr -> if (tr.date.getDayOfMonth() == numberOfWeek) tr else null }
}