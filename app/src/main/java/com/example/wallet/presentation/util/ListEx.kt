package com.example.wallet.presentation.util

import android.util.Log
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