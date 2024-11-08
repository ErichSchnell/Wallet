package com.example.wallet.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wallet.presentation.model.EventItem
import com.example.wallet.presentation.util.BackgroundScreen
import com.example.wallet.presentation.util.ScaffoldWallet
import com.example.wallet.presentation.util.ShimmerHome
import com.example.wallet.presentation.util.ShowToast
import com.example.wallet.presentation.util.getMonthAndYearString
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userEmail: String,
    navigateToLogin: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    val uiState by homeViewModel.uiState.collectAsState()


    val pagerState = rememberPagerState(initialPage = uiState.screens.id) { 3 }
    val scope = rememberCoroutineScope()


    LaunchedEffect(true) {
        homeViewModel.getAccount(userEmail)
    }
    LaunchedEffect(pagerState.currentPage) {
        homeViewModel.navigateToForId(pagerState.currentPage)
    }



    ShimmerHome(
        isLoading = uiState.isLoading,
        contentAfterLoading = {

            ScaffoldWallet(
                screen = uiState.screens,
                onClickNavigationIcon = {
                    when (uiState.screens.id) {
                        Screens.INCOME.id,
                        Screens.EXPENSES.id,
                        Screens.OVERVIEW.id -> homeViewModel.logout(navigateToLogin = navigateToLogin)
                        Screens.SETTING.id -> {
                            homeViewModel.navigateToForId(Screens.OVERVIEW.id)
                            scope.launch { pagerState.scrollToPage(Screens.OVERVIEW.id) }
                        }
                    }
                },

                transactionList = when (uiState.screens.id) {
                    Screens.INCOME.id ->  uiState.incomes.transactions
                    Screens.EXPENSES.id ->  uiState.expenses.transactions
                    Screens.OVERVIEW.id ->  uiState.home.transactions
                    else ->  uiState.home.transactions
                },
                transactionBookmark = when (uiState.screens.id) {
                    Screens.INCOME.id ->  uiState.incomes.transactionsBookmark
                    Screens.EXPENSES.id ->  uiState.expenses.transactionsBookmark
                    Screens.OVERVIEW.id ->  uiState.home.transactionsBookmark
                    else ->  uiState.home.transactions
                },

                profiles = uiState.profiles,
                profileSelected = uiState.profileSelected,
                onProfileChanged = { profile -> homeViewModel.changeProfile(profile) },

                onClickSetting = { homeViewModel.navigateToForId(Screens.SETTING.id) },

                showBottomBar = (
                        uiState.screens == Screens.INCOME ||
                        uiState.screens == Screens.EXPENSES ||
                        uiState.screens == Screens.OVERVIEW
                ),
                onClickBottomNavigation = { screenId ->
                    homeViewModel.navigateToForId(screenId)
                    scope.launch { pagerState.scrollToPage(screenId) }
                },

                onClickEventItem = { item, event ->
                    when(event){
                        EventItem.EDIT,
                        EventItem.ADD,
                        EventItem.BOOKMARK -> homeViewModel.addTransaction(item)
                        EventItem.DELETE -> homeViewModel.removeItem(item.id)
                    }
                },

                onClickAddCategory = {category -> homeViewModel.addCategory(category)}
            ) { topPadding, bottomPadding ->

                when (uiState.screens.id) {
                    Screens.INCOME.id,
                    Screens.EXPENSES.id,
                    Screens.OVERVIEW.id -> {

                        HorizontalPager( state = pagerState) { currentPage ->
                            Box {
                                val bg = when (currentPage) {
                                    Screens.INCOME.id -> Screens.INCOME.bg
                                    Screens.EXPENSES.id -> Screens.EXPENSES.bg
                                    Screens.OVERVIEW.id -> Screens.OVERVIEW.bg
                                    else -> Screens.OVERVIEW.bg
                                }
                                BackgroundScreen(bg)

                                when (currentPage) {
                                    Screens.INCOME.id -> {
                                        BudgetWallet(
                                            topPadding = topPadding,
                                            bottomPadding = bottomPadding,
                                            detailsScreen = uiState.incomes,
                                            onClickNextCategory = {category ->  homeViewModel.changeCategory(category)},
                                            dateSelected = uiState.dateSelected?.getMonthAndYearString().orEmpty(),
                                            onClickPreviousMonth = {homeViewModel.previousMonth()},
                                            onClickNextMonth = {homeViewModel.nextMonth()},
                                        )
                                    }
                                    Screens.EXPENSES.id -> {
                                        BudgetWallet(
                                            topPadding = topPadding,
                                            bottomPadding = bottomPadding,
                                            detailsScreen = uiState.expenses,
                                            onClickNextCategory = {category ->  homeViewModel.changeCategory(category)},
                                            onClickPreviousMonth = {homeViewModel.previousMonth()},
                                            dateSelected = uiState.dateSelected?.getMonthAndYearString().orEmpty(),
                                            onClickNextMonth = {homeViewModel.nextMonth()},
                                        )
                                    }
                                    Screens.OVERVIEW.id -> {
                                        OverviewWallet(
                                            topPadding = topPadding,
                                            bottomPadding = bottomPadding,
                                            detailsScreen = uiState.home,
                                            detailsScreenIncome = uiState.incomes,
                                            detailsScreenExpenses = uiState.expenses,
                                            dateSelected = uiState.dateSelected?.getMonthAndYearString().orEmpty(),
                                            onClickPreviousMonth = {homeViewModel.previousMonth()},
                                            onClickNextMonth = {homeViewModel.nextMonth()},
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }

                    Screens.SETTING.id -> {
                        ProfileSetting(
                            topPadding = topPadding,
                            profileSelected = uiState.profileSelected,
                            onClickEditCategory = {homeViewModel.addCategory(category = it)},
                            onClickDeleteCategory = {homeViewModel.deleteCategory(category = it)}
                        )

                    }
                }

            }
        }
    )

    BackHandler {
        if (uiState.screens == Screens.SETTING) {
            homeViewModel.selectItemTransaction(null)
            homeViewModel.navigateToForId(Screens.OVERVIEW.id)
            scope.launch { pagerState.scrollToPage(Screens.OVERVIEW.id) }
        } else {
            activity?.moveTaskToBack(true)  //minimizer app
        }
    }

    ShowToast(stringResource(uiState.toastText)) {
        homeViewModel.clearToast()
    }
    ShowToast(uiState.toastTextExeption) {
        homeViewModel.clearToastExeption()
    }
}
