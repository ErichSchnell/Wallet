package com.example.wallet.presentation.model

import com.example.wallet.presentation.util.ex.mapWithCategory
import com.example.wallet.ui.theme.OnHomeContainer

data class DetailsScreen(
    val transactions: List<TransactionModelUI> = emptyList(),
    val categorySelected: CategoryUi = CategoryUi(),
    val categories: List<CategoryUi> = emptyList(),
    val transactionsBookmark: List<TransactionModelUI> = emptyList(),
) {
    fun update(
        newDetails: DetailsScreen,
    ): DetailsScreen {


        val ctSelected = newDetails.categories.find { it.id == categorySelected.id } ?: newDetails.categories[0]
        val tr = newDetails.transactions.mapWithCategory(ctSelected)

        return this.copy(
            transactions = tr,
            categories = newDetails.categories,
            categorySelected = ctSelected,
            transactionsBookmark = newDetails.transactionsBookmark
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

