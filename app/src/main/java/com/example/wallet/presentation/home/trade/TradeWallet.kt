package com.example.wallet.presentation.home.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.home.overview.ItemTrackTransactionPercent
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.DetailsScreen
import com.example.wallet.presentation.home.MonthFilter
import com.example.wallet.presentation.util.composables.CardWallet
import com.example.wallet.presentation.util.ex.adjustBrightness

@Composable
fun TradeWallet(
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
    ) {
        if (dateSelected.isNotEmpty()) {
            MonthFilter(
                date = dateSelected,
                onClickPreviousMonth = onClickPreviousMonth,
                onClickNextMonth = onClickNextMonth
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            if (transaction.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))

                CategoryAmount(
                    category = detailsScreen.categorySelected,
                    categories = detailsScreen.categories,
                    onClickNextCategory = { category -> onClickNextCategory(category) },
                )

                GraficoByWeek("SemanasDelMes x Monto", transaction)

                GraficoByDay("DiasDelMes x Monto", transaction)

//                TransactionsPointsGraph(
//                    transactionWallet = transaction,
//                    amount = detailsScreen.categories[0].amount
//                )
//
//                GraficoTransacciones(title = "Transactions", transactions = transaction)
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(bottomPadding + 88.dp)
            )
        }
    }


}

@Composable
fun PercentageByCategory(categorySelected: CategoryUi, categories: List<CategoryUi>) {
    val amountTotal = categories[0].amount

    CardWallet {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (categorySelected == categories[0]) {
                ItemProgressCategoryTotal(modifier = Modifier.fillMaxWidth().height(32.dp), categories = categories)
            } else {
                ItemTrackTransactionPercent(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    progress = (categorySelected.amount / amountTotal).toFloat(),
                    progressColor = categorySelected.color.adjustBrightness(0.5f),
                    backgroundColor = categorySelected.color,
                )
            }
        }
    }
}

@Composable
fun ItemProgressCategoryTotal(
    modifier: Modifier,
    categories: List<CategoryUi>
) {
    val amountTotal = categories[0].amount.toFloat()

    if (categories.size == 2) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                progress = { 0f },
                trackColor = categories[1].color.adjustBrightness(0.5f),
            )
            LinearProgressIndicator(
                modifier = Modifier.padding(8.dp).fillMaxSize().clip(CircleShape),
                progress = { 1f },
                color = categories[1].color,
                trackColor = categories[1].color.adjustBrightness(0.5f),
            )
            Text(text = "100%", color = Color.Black)
        }
    } else {
        BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
            val xStep = maxWidth.value / amountTotal
            Row(Modifier.fillMaxSize().clip(CircleShape)) {
                categories.forEachIndexed { index, categoryUi ->
                    if (index != 0) {
                        val width = categoryUi.amount.toFloat() * xStep
                        val color = categoryUi.color.adjustBrightness(0.5f)
                        Box(Modifier.fillMaxHeight().width(width.dp).background(color))
                    }
                }
            }
            Row(Modifier.fillMaxSize().padding(8.dp).clip(CircleShape)) {
                categories.forEachIndexed { index, categoryUi ->
                    if (index != 0) {
                        val width = categoryUi.amount.toFloat() * xStep
                        val color = categoryUi.color
                        Box(Modifier.fillMaxHeight().width(width.dp).background(color))
                    }
                }
            }
            Text(text = "100%", color = Color.Black)
        }
    }
}

@Composable
fun CategoryAmount(
    category: CategoryUi,
    categories: List<CategoryUi>,
    onClickNextCategory: (CategoryUi) -> Unit,
) {
    val indexCurrent = categories.indexOf(categories.find { it == category }).takeIf { it != -1 } ?: 0
    val categorySelected = categories[indexCurrent]

    CardWallet(height = 348.dp) {

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$${categorySelected.amount}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(16.dp))
                Icon(
                    modifier = Modifier
                        .clickable {
                            val indexPost = when {
                                indexCurrent < 1 -> {
                                    categories.size - 1
                                }

                                else -> {
                                    indexCurrent - 1
                                }
                            }
                            onClickNextCategory(categories[indexPost])
                        }
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_chevron_left_circle),
                    contentDescription = ""
                )
                WalletCategoryGraph(
                    modifier = Modifier.weight(1f),
                    categorySelected = categorySelected,
                    categories = categories,
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            val indexPost = when {
                                indexCurrent < categories.size - 1 -> {
                                    indexCurrent + 1
                                }

                                else -> {
                                    0
                                }
                            }
                            onClickNextCategory(categories[indexPost])
                        }
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_chevron_right_circle),
                    contentDescription = ""
                )
                Spacer(Modifier.width(16.dp))
            }

            Box (Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)){
                if (categorySelected == categories[0]) {
                    ItemProgressCategoryTotal(modifier = Modifier.fillMaxWidth().height(32.dp), categories = categories)
                } else {
                    ItemTrackTransactionPercent(
                        modifier = Modifier.fillMaxWidth().height(32.dp),
                        progress = (categorySelected.amount / categories[0].amount).toFloat(),
                        progressColor = categorySelected.color.adjustBrightness(0.5f),
                        backgroundColor = categorySelected.color,
                    )
                }
            }


            Spacer(Modifier.width(16.dp))

            WalletSelectorGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                countItems = categories.size,
                itemSelected = indexCurrent
            )
            Spacer(Modifier.height(8.dp))
        }
    }

}



