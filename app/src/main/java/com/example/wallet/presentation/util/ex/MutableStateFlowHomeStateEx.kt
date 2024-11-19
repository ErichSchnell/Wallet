package com.example.wallet.presentation.util.ex

import android.util.Log
import com.example.wallet.presentation.home.HomeUIState
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.DetailsScreen
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.withNewId
import com.example.wallet.presentation.model.TypeOfTransaction
import com.example.wallet.presentation.model.TypeOfTransaction.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


fun MutableStateFlow<HomeUIState>.setTransactionWithCategory(transactions: List<TransactionModelUI> = emptyList()){
    val categories = this.value.profileSelected?.categories
    val tr = transactions.setWithCategory(categories)
    this.update {it.copy(allTransactions = tr)}
}
fun MutableStateFlow<HomeUIState>.updateDate(){
    val monthsUsed = this.value.allTransactions.reversed().mapNotNull { it.date?.getMonthAndYearString() }.distinct()
    val monthSelected:String?

    monthSelected = if (monthsUsed.isNotEmpty()){
        monthsUsed.find { it == this.value.dateSelected } ?: monthsUsed.last()
    } else null

    Log.i("TAG ERICH", "monthSelected: $monthSelected")

    this.update {it.copy(monthList = monthsUsed.toList(), dateSelected = monthSelected)}
}



fun MutableStateFlow<HomeUIState>.updateDetailsScreens(){
    val home = this.mapBudget(EMPTY)
    val income = this.mapBudget(INCOME)
    val expenses = this.mapBudget(EXPENSES)

    this.update { uiState ->
        uiState.copy(
            home = uiState.home.update(home),
            incomes = uiState.incomes.update(income),
            expenses = uiState.expenses.update(expenses),
        )
    }
}
fun MutableStateFlow<HomeUIState>.mapBudget(budget: TypeOfTransaction): DetailsScreen {
    val listBudget = this.value.allTransactions.mapNotNull { trans ->
        when(budget){
            EMPTY -> trans
            else -> { if (trans.budget == budget) trans else null}
        }
    }
    val listBookmark = listBudget.mapNotNull { if (it.isBookmark) it else null }
    val listFilterDate = listBudget.mapWithDate(this.value.dateSelected)
    val categories = listFilterDate.getCategories(this.value.profileSelected?.categories)

    return DetailsScreen(
        transactions = listFilterDate,
        transactionsBookmark = listBookmark,
        categories = categories,
    )
}



fun MutableStateFlow<HomeUIState>.getProfileWithNewCategory(category: CategoryUi): ProfileModelUi?{
    val profileAux = this.value.profileSelected
    profileAux ?: return null

    val categories = profileAux.categories.toMutableList()

    val existId = categories.find { it.id == category.id }
    val index = categories.indexOf(existId)

    if (index >= 0){
        categories[index] = category
    } else {
        categories.add(category.withNewId())
    }

    return profileAux.copy(categories = categories.toList())
}
fun MutableStateFlow<HomeUIState>.getProfileRemoveCategory(category: CategoryUi): ProfileModelUi?{
    val profileAux = this.value.profileSelected
    profileAux ?: return null

    val categories = profileAux.categories.toMutableList()
    categories.remove(category)

    return profileAux.copy(categories = categories.toList())
}


