package com.example.wallet.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.DetailsScreen
import com.example.wallet.presentation.util.CalendarFilter
import com.example.wallet.presentation.util.CardWallet
import com.example.wallet.presentation.util.GraficoTransacciones
import com.example.wallet.presentation.util.WalletCategoryGraph
import com.example.wallet.presentation.util.WalletSelectorGraph
import com.example.wallet.presentation.util.adjustBrightness
import com.example.wallet.presentation.util.getMonthAndYearString
import java.util.Date

@Composable
fun BudgetWallet(
    topPadding: Dp,
    bottomPadding: Dp,
    detailsScreen: DetailsScreen,
    onClickNextCategory: (CategoryUi) -> Unit,
    dateSelected: String,
    onClickPreviousMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val transaction = detailsScreen.transactions.reversed()

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = topPadding)
            .verticalScroll(scrollState)
    ) {
        CalendarFilter(
            date = dateSelected,
            onClickPreviousMonth = onClickPreviousMonth,
            onClickNextMonth = onClickNextMonth
        )
        if (transaction.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))

            CategoryAmount(
                category = detailsScreen.categorySelected,
                categorys = detailsScreen.categories,
                onClickNextCategory = {category -> onClickNextCategory(category)},
            )

            PercentageByCategory(detailsScreen.categorySelected, detailsScreen.categories)

            TransactionsPointsGraph(transactionWallet = transaction, amount = detailsScreen.categories[0].amount)
            GraficoTransacciones(title = "Transactions", transactions = transaction)
//            GraficoAcumulado(transaction)
        }

        Box(Modifier.fillMaxWidth().height(bottomPadding + 88.dp))
    }
}

@Composable
fun PercentageByCategory(categorySelected: CategoryUi, categories: List<CategoryUi>) {
    val amountTotal = categories[0].amount

    CardWallet {
        Column (
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if (categorySelected == categories[0]){
                categories.forEachIndexed { index, ct ->
                    if (index != 0){
                        ItemTrackTransactionPercent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            progress = (ct.amount / amountTotal).toFloat(),
                            progressColor = ct.color.adjustBrightness(0.5f),
                            backgroundColor = ct.color,
                        )
                        if (index != (categories.size - 1 ))
                            Spacer(Modifier.height(16.dp))
                    }
                }
            } else {
                ItemTrackTransactionPercent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    progress = (categorySelected.amount / amountTotal).toFloat(),
                    progressColor = categorySelected.color.adjustBrightness(0.5f),
                    backgroundColor = categorySelected.color,
                )
            }
        }

    }
}

@Composable
fun CategoryAmount(
    category: CategoryUi,
    categorys: List<CategoryUi>,
    onClickNextCategory: (CategoryUi) -> Unit,
) {
    val indexCurrent = categorys.indexOf(categorys.find { it == category }).takeIf { it != -1 } ?: 0
    val categorySelected = categorys[indexCurrent]

    CardWallet(height = 300.dp) {

        Column( Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$${categorySelected.amount}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row (
                Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalAlignment = Alignment.CenterVertically){
                Spacer(Modifier.width(16.dp))
                Icon(
                    modifier = Modifier
                        .clickable {
                            val indexPost = when {
                                indexCurrent < 1 -> {
                                    categorys.size - 1
                                }

                                else -> {
                                    indexCurrent - 1
                                }
                            }
                            onClickNextCategory(categorys[indexPost])
                        }
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_chevron_left_circle), contentDescription = ""
                )
                WalletCategoryGraph(
                    modifier = Modifier.weight(1f),
                    categorySelected = categorySelected,
                    categories = categorys,
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            val indexPost = when {
                                indexCurrent < categorys.size - 1 -> {
                                    indexCurrent + 1
                                }

                                else -> {
                                    0
                                }
                            }
                            onClickNextCategory(categorys[indexPost])
                        }
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_chevron_right_circle), contentDescription = ""
                )
                Spacer(Modifier.width(16.dp))
            }

            WalletSelectorGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                countItems = categorys.size,
                itemSelected = indexCurrent
            )
            Spacer(Modifier.height(8.dp))
        }
    }

}



