package com.example.wallet.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.presentation.model.DetailsScreen
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.util.CalendarFilter
import com.example.wallet.presentation.util.CardWallet
import com.example.wallet.presentation.util.GraficoTransacciones
import com.example.wallet.presentation.util.WalletResumeChartGraph
import com.example.wallet.presentation.util.getMonthAndYearString
import java.util.Date
import kotlin.math.abs

@Composable
fun OverviewWallet(
    topPadding: Dp,
    bottomPadding: Dp,
    detailsScreen: DetailsScreen,
    detailsScreenIncome: DetailsScreen,
    detailsScreenExpenses: DetailsScreen,
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
        CalendarFilter(
            date = dateSelected,
            onClickPreviousMonth = onClickPreviousMonth,
            onClickNextMonth = onClickNextMonth
        )
        Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(scrollState)) {
            if (transaction.isNotEmpty()) {

                TransactionsPointsGraph(transactionWallet = transaction.reversed(), amount = detailsScreen.categories[0].amount)

                ResumeTransactionsPercent(
                    amountIncome = detailsScreenIncome.categories[0].amount.toFloat(),
                    amountExpenses = detailsScreenExpenses.categories[0].amount.toFloat(),
                )

                GraficoTransacciones(title = "Transactions", transactions = transaction)
            }

            Box(Modifier
                .fillMaxWidth()
                .height(bottomPadding + 88.dp))
        }
    }

}

@Composable
fun TransactionsPointsGraph(
    transactionWallet: List<TransactionModelUI>,
    amount: Double,
) {
    if (transactionWallet.isEmpty()) return
    CardWallet(height = 300.dp) {
        Column( Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$$amount",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            WalletResumeChartGraph(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                transactions = transactionWallet,
                pointColor = MaterialTheme.colorScheme.onBackground,
                pathColor = MaterialTheme.colorScheme.onBackground,
                ejeColor = MaterialTheme.colorScheme.secondary
            )
        }

    }
}


@Composable
fun ResumeTransactionsPercent(
    amountIncome: Float,
    amountExpenses: Float
) {
    val amountTotal = amountIncome + abs(amountExpenses)

    CardWallet {
        Column (
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            ItemTrackTransactionPercent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                progress = amountIncome / amountTotal,
                progressColor = Screens.INCOME.colors.container,
                backgroundColor = Screens.INCOME.colors.onContainer,
            )

            Spacer(Modifier.height(16.dp))

            ItemTrackTransactionPercent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                progress = abs(amountExpenses) / amountTotal,
                progressColor = Screens.EXPENSES.colors.container,
                backgroundColor = Screens.EXPENSES.colors.onContainer,
            )
        }

    }
}

@Composable
fun ItemTrackTransactionPercent(
    progress:Float = 0f,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    progressColor: Color,
) {
    val percent:Int = (progress * 100f).toInt()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxSize().clip(CircleShape),
            progress = { 0f },
            trackColor = progressColor,
        )
        LinearProgressIndicator(
            modifier = Modifier.padding(8.dp).fillMaxSize().clip(CircleShape),
            progress = { progress },
            color = backgroundColor,
            trackColor = progressColor,
        )
        Text(text = "$percent%", color = Color.Black)
    }
}


