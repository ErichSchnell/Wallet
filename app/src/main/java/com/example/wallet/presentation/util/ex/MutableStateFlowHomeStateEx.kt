package com.example.wallet.presentation.util.ex

import android.util.Log
import com.example.wallet.presentation.home.HomeUIState
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.withNewId
import com.example.wallet.presentation.model.TypeOfTransaction
import com.example.wallet.presentation.model.TypeOfTransaction.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


fun MutableStateFlow<HomeUIState>.setTransactionWithCategory(transactions: List<TransactionModelUI>){
    val categories = this.value.profileSelected?.categories
    val tr = transactions.setWithCategory(categories)
    this.update {it.copy(allTransactions = tr)}
}
fun MutableStateFlow<HomeUIState>.updateDate(){
    val monthsUsed = this.value.allTransactions.reversed().mapNotNull { it.date?.getMonthAndYearString() }.distinct()
    val monthSelected = monthsUsed.find { it == this.value.dateSelected }

    Log.i("TAG ERICH", "monthSelected: $monthSelected")

    if (monthSelected != null){
        this.update {it.copy(monthList = monthsUsed.toList())}
    } else {
        this.update {it.copy(monthList = monthsUsed.toList(), dateSelected = monthsUsed.last())}
    }
}



fun MutableStateFlow<HomeUIState>.updateTransactions(){
    val home = this.mapBudget(EMPTY)
    val income = this.mapBudget(INCOME)
    val expenses = this.mapBudget(EXPENSES)

    this.update { uiState ->
        uiState.copy(
            home = uiState.home.update(home.first,home.second),
            incomes = uiState.incomes.update(income.first,income.second),
            expenses = uiState.expenses.update(expenses.first,expenses.second),
        )
    }
}
fun MutableStateFlow<HomeUIState>.mapBudget(budget: TypeOfTransaction): Pair<List<TransactionModelUI>, List<CategoryUi>> {
    val listBudget = this.value.allTransactions.mapNotNull { trans ->
        when(budget){
            EMPTY -> trans
            else -> { if (trans.budget == budget) trans else null}
        }
    }
    val listFilterDate = listBudget.mapWithDate(this.value.dateSelected)
    val categories = listFilterDate.getCategories(this.value.profileSelected?.categories)

    return Pair(listFilterDate,categories)
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


