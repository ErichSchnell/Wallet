package com.example.wallet.presentation.model

import android.util.Log
import com.example.wallet.presentation.util.getMonthAndYearString
import com.example.wallet.presentation.util.mapWithCategory
import com.example.wallet.ui.theme.OnHomeContainer
import java.util.Date

data class DetailsScreen(
    val transactions: List<TransactionModelUI> = emptyList(),
    val categorySelected: CategoryUi = CategoryUi(),
    val categories: List<CategoryUi> = emptyList(),
    val transactionsBookmark: List<TransactionModelUI> = emptyList(),
) {
    fun update(
        transactions: List<TransactionModelUI>,
        categories:  List<CategoryUi>,
    ): DetailsScreen {


        val ctSelected = categories.find { it.id == categorySelected.id } ?: categories[0]
        val tr = transactions.mapWithCategory(ctSelected)

        return this.copy(
            transactions = tr,
            categories = categories,
            categorySelected = ctSelected,
            transactionsBookmark = tr.mapNotNull { if (it.isBookmark) it else null },
        )
    }
}

private fun getCategoriesWithAmount(tr: List<TransactionModelUI>, categories: List<CategoryUi>?): List<CategoryUi>{
    categories ?: return emptyList()

    val categoriesTitles = mutableListOf("Total")
    categoriesTitles.addAll(tr.map { it.category.id }.distinct())

    var index = 0

    return categoriesTitles.map { categoryId ->
        val ct = categories.find { it.id == categoryId }

        val cate = if (ct != null){
            ct.copy(amount = tr.sumOf { if (it.category.id == categoryId) it.amount else .0 })
        } else {
            CategoryUi(
                id = if (index == 0) "Total" else "",
                name = if (index == 0) "Total" else "",
                amount = if (index == 0) tr.sumOf { it.amount } else tr.sumOf { if (it.category.id == categoryId) it.amount else .0 },
                color = OnHomeContainer
            )
        }
        index++

        cate
    }
}

