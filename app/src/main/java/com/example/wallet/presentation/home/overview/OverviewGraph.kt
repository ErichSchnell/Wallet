package com.example.wallet.presentation.home.overview

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.presentation.home.trade.calculateTextSize
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.util.composables.CardWallet
import com.example.wallet.presentation.util.ex.getWeek
import com.example.wallet.ui.theme.WalletColors
import kotlin.math.abs
import kotlin.math.max


@Composable
fun WalletResumeChartGraph(
    modifier: Modifier = Modifier,
    transactions: List<TransactionModelUI>,
    pointColor: Color,
    pathColor: Color,
    ejeColor: Color,
) {
    BoxWithConstraints(modifier) {
        val heigh = maxHeight
        val with = maxWidth
        var amount = 0f
        var showEjeX = false
        val listTransactions = transactions.reversed().map {
            amount += it.amount.toFloat()
            amount
        }

        val maxAmount = listTransactions.max()
        val minAmount = listTransactions.min()

        if (maxAmount >= 0 && minAmount <= 0) showEjeX = true

        Canvas(modifier = Modifier.fillMaxSize()) {
            val pathTransactionsIncome = Path()
            val pathTransactionsExpenses = Path()
            val pathGraph = Path()

            val xStep = size.width / (listTransactions.size - 1)
            val yStep = size.height / (maxAmount - minAmount)

            var preAmount = 0f
            var xPre = 0f
            var yPre = 0f

            listTransactions.forEachIndexed { index, amount ->


                val xPos = index * xStep
                val yPos = size.height - ((amount - minAmount) * yStep)

                if (index != 0) {
                    if (amount >= preAmount) {
                        pathTransactionsIncome.moveTo(xPre, yPre)
                        pathTransactionsIncome.lineTo(xPos, yPos)
                    } else {
                        pathTransactionsExpenses.moveTo(xPre, yPre)
                        pathTransactionsExpenses.lineTo(xPos, yPos)
                    }
                }
                preAmount = amount
                xPre = xPos
                yPre = yPos

                drawCircle(
                    color = pointColor,
                    radius = 4.dp.toPx(),
                    center = Offset(xPos, yPos)
                )

            }

            if (showEjeX) {
                pathGraph.apply {
                    val yPos = size.height - ((0 - minAmount) * yStep)
                    moveTo(0f, yPos)
                    lineTo(size.width, yPos)
                }
                drawPath(pathGraph, color = ejeColor, style = Stroke(width = 1.dp.toPx()))
            }



            drawPath(
                path = pathTransactionsIncome,
                color = WalletColors.icome().container,
                style = Stroke(width = 2.dp.toPx())
            )
            drawPath(
                path = pathTransactionsExpenses,
                color = WalletColors.expenses().container,
                style = Stroke(width = 2.dp.toPx())
            )
        }


    }

}


@Composable
fun GraficoAcumulado(transaction: List<TransactionModelUI>) {
    var amount = .0
    val transactionAcumulado = transaction.map {
        amount += it.amount
        it.copy(amount = amount)
    }
    GraficoTransacciones(title = "Resto Mensual: ${transactionAcumulado.last().amount}", transactions = transactionAcumulado)
}

@Composable
fun GraficoTransacciones(
    title: String,
    transactions: List<TransactionModelUI>
) {
//    val amountTotal = transactions.sumOf { it.amount }
    val maxValue = transactions.maxOfOrNull { it.amount } ?: .0
    val minValue = transactions.minOfOrNull { it.amount } ?: .0

    CardWallet(height = 300.dp) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            when{
                maxValue > 0 && minValue >= 0  -> GraficoPositivo(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    transactions = transactions
                )
                maxValue <= 0 && minValue < 0  -> GraficoNegativo(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    transactions = transactions
                )
                else -> GraficoReal(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    transactions = transactions
                )
            }
        }
    }
}

@Composable
fun GraficoPositivo(
    modifier: Modifier = Modifier,
    transactions: List<TransactionModelUI>,
) {
    val maxAmount = transactions.map { it.amount.toFloat() }.max()
    Row(modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "$$maxAmount")
            Spacer(Modifier.weight(1f))
            Text(text = "$0")
        }
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val pointsList = mutableListOf<Pair<Offset, Color>>()
            val pathTransactionsIncome = Path()
            val pathTransactionsExpenses = Path()
            var preAmount = 0f
            var xPre = 0f
            var yPre = 0f

            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / maxAmount


            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = (index * xStep) + widthStartBars + xStep
                val yPos = size.height - (amount * yStep)
                if (index != 0) {
                    if (amount >= preAmount) {
                        pathTransactionsIncome.moveTo(xPre, yPre)
                        pathTransactionsIncome.lineTo(xPos, yPos)
                    } else {
                        pathTransactionsExpenses.moveTo(xPre, yPre)
                        pathTransactionsExpenses.lineTo(xPos, yPos)
                    }
                }
                preAmount = amount
                xPre = xPos
                yPre = yPos
                pointsList.add(Pair(Offset(xPos, yPos), tr.category.color))
            }


            val path = Path()
            path.moveTo(widthStartGuide, 0f)
            path.lineTo(widthStartGuide, size.height)
            path.moveTo(widthStartGuide, size.height)
            path.lineTo(size.width, size.height)
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.5f),
                style = Stroke(width = 1.dp.toPx())
            )
            drawPath(
                path = pathTransactionsIncome,
                color = WalletColors.icome().container,
                style = Stroke(width = 2.dp.toPx())
            )
            drawPath(
                path = pathTransactionsExpenses,
                color = WalletColors.expenses().container,
                style = Stroke(width = 2.dp.toPx())
            )
            pointsList.forEach {
                drawCircle(
                    color = it.second,
                    radius = 4.dp.toPx(),
                    center = it.first
                )
            }
        }
    }
}

@Composable
fun GraficoNegativo(
    modifier: Modifier = Modifier,
    transactions: List<TransactionModelUI>,
) {
    val minAmount = transactions.map { it.amount.toFloat() }.min()
    Row(modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "$0")
            Spacer(Modifier.weight(1f))
            Text(text = "$$minAmount")
        }
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val pointsList = mutableListOf<Pair<Offset, Color>>()
            val pathTransactionsIncome = Path()
            val pathTransactionsExpenses = Path()
            var preAmount = 0f
            var xPre = 0f
            var yPre = 0f

            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / abs(minAmount)

            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = (index * xStep) + widthStartBars + xStep
                val yPos = abs((amount * yStep))

                if (index != 0) {
                    if (amount >= preAmount) {
                        pathTransactionsIncome.moveTo(xPre, yPre)
                        pathTransactionsIncome.lineTo(xPos, yPos)
                    } else {
                        pathTransactionsExpenses.moveTo(xPre, yPre)
                        pathTransactionsExpenses.lineTo(xPos, yPos)
                    }
                }
                preAmount = amount
                xPre = xPos
                yPre = yPos
                pointsList.add(Pair(Offset(xPos, yPos), tr.category.color))
            }


            val path = Path()
            path.moveTo(widthStartGuide, 0f)
            path.lineTo(widthStartGuide, size.height)

            path.moveTo(widthStartGuide, 0f)
            path.lineTo(size.width, 0f)
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.5f),
                style = Stroke(width = 1.dp.toPx())
            )
            drawPath(
                path = pathTransactionsIncome,
                color = WalletColors.icome().container,
                style = Stroke(width = 2.dp.toPx())
            )
            drawPath(
                path = pathTransactionsExpenses,
                color = WalletColors.expenses().container,
                style = Stroke(width = 2.dp.toPx())
            )
            pointsList.forEach {
                drawCircle(
                    color = it.second,
                    radius = 4.dp.toPx(),
                    center = it.first
                )
            }
        }
    }
}

@Composable
fun GraficoReal(
    modifier: Modifier = Modifier,
    transactions: List<TransactionModelUI>,
) {
    val maxAmount = transactions.maxOfOrNull { it.amount.toFloat() } ?: 0f
    val minAmount = transactions.minOfOrNull { it.amount.toFloat() } ?: 0f


    Row(modifier) {
        EjesGraph(
            modifier = Modifier.fillMaxHeight(),
            maxValue = maxAmount,
            minValue = minAmount,
        )

        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {

            val pointsList = mutableListOf<Pair<Offset, Color>>()
            val pathTransactionsIncome = Path()
            val pathTransactionsExpenses = Path()
            var preAmount = 0f
            var xPre = 0f
            var yPre = 0f

            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / (maxAmount - minAmount)
            val yZero = size.height - abs(minAmount) * yStep

            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = (index * xStep) + widthStartBars + xStep
                val yPos = size.height - ((amount - minAmount) * yStep)

                if (index != 0) {
                    if (amount >= preAmount) {
                        pathTransactionsIncome.moveTo(xPre, yPre)
                        pathTransactionsIncome.lineTo(xPos, yPos)
                    } else {
                        pathTransactionsExpenses.moveTo(xPre, yPre)
                        pathTransactionsExpenses.lineTo(xPos, yPos)
                    }
                }
                preAmount = amount
                xPre = xPos
                yPre = yPos
                pointsList.add(Pair(Offset(xPos, yPos), tr.category.color))
            }


            val path = Path()
            path.moveTo(widthStartGuide, 0f)
            path.lineTo(widthStartGuide, size.height)

            path.moveTo(widthStartGuide, yZero)
            path.lineTo(size.width, yZero)
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.5f),
                style = Stroke(width = 1.dp.toPx())
            )
            drawPath(
                path = pathTransactionsIncome,
                color = WalletColors.icome().container,
                style = Stroke(width = 2.dp.toPx())
            )
            drawPath(
                path = pathTransactionsExpenses,
                color = WalletColors.expenses().container,
                style = Stroke(width = 2.dp.toPx())
            )
            pointsList.forEach {
                drawCircle(
                    color = it.second,
                    radius = 4.dp.toPx(),
                    center = it.first
                )
            }
        }
    }



}



@Composable
fun GraficoByWeek(
    title: String,
    transactionsIncome: List<TransactionModelUI>,
    transactionsExpenses: List<TransactionModelUI>,
) {
    val trIncomeAbs = transactionsIncome.map { it.copy(amount = abs(it.amount)) }
    val trExpensesAbs = transactionsExpenses.map { it.copy(amount = abs(it.amount)) }

    val trIncome = listOf(
        trIncomeAbs.getWeek(1).sumOf { it.amount }.toFloat(),
        trIncomeAbs.getWeek(2).sumOf { it.amount }.toFloat(),
        trIncomeAbs.getWeek(3).sumOf { it.amount }.toFloat(),
        trIncomeAbs.getWeek(4).sumOf { it.amount }.toFloat(),
    )
    val trExpenses = listOf(
        trExpensesAbs.getWeek(1).sumOf { it.amount }.toFloat(),
        trExpensesAbs.getWeek(2).sumOf { it.amount }.toFloat(),
        trExpensesAbs.getWeek(3).sumOf { it.amount }.toFloat(),
        trExpensesAbs.getWeek(4).sumOf { it.amount }.toFloat(),
    )
    val maxAmount = max(trIncome.max(),trExpenses.max())

    CardWallet(height = 300.dp) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)

            Row(Modifier.padding(16.dp).weight(1f).fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(), horizontalAlignment = Alignment.End) {
                    Text(text = "$$maxAmount")
                    Spacer(Modifier.weight(1f))
                    Text(text = "$0")
                }
                Canvas(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()) {
                    val width = size.width * 0.85f
                    val xStep = width / 4
                    val yStep = size.height / maxAmount

                    val widthStartBars = (size.width - width) / 2f
                    val widthStartGuide = (size.width - width) / 2f

                    for (index in 0 until 4) {
                        val xPos = index * xStep
                        val yPosIncome = size.height - (trIncome[index] * yStep)
                        val yPosExpense = size.height - (trExpenses[index] * yStep)

                        if (yPosIncome < yPosExpense){
                            drawLine(
                                color = WalletColors.icome().container,
                                start = Offset(xPos + widthStartBars + xStep, size.height),
                                end = Offset(xPos + widthStartBars + xStep, yPosIncome),
                                strokeWidth = xStep * .75f
                            )
                            drawLine(
                                color = WalletColors.expenses().container.copy(alpha = 0.7f),
                                start = Offset(xPos + widthStartBars + xStep, size.height),
                                end = Offset(xPos + widthStartBars + xStep, yPosExpense),
                                strokeWidth = xStep * .75f
                            )
                        } else {
                            drawLine(
                                color = WalletColors.expenses().container,
                                start = Offset(xPos + widthStartBars + xStep, size.height),
                                end = Offset(xPos + widthStartBars + xStep, yPosExpense),
                                strokeWidth = xStep * .75f
                            )
                            drawLine(
                                color = WalletColors.icome().container.copy(alpha = 0.7f),
                                start = Offset(xPos + widthStartBars + xStep, size.height),
                                end = Offset(xPos + widthStartBars + xStep, yPosIncome),
                                strokeWidth = xStep * .75f
                            )
                        }

                        val textPaint = TextPaint().apply {
                            color = Color.White.toArgb() // Color del texto
                            textSize = calculateTextSize(4, 8.sp.toPx(), 12.sp.toPx())         // Tamaño del texto
                            isAntiAlias = true           // Para bordes más suaves
                        }
                        val text = "s°${index + 1}"
                        val textWidth = textPaint.measureText(text)
                        drawContext.canvas.nativeCanvas.drawText(
                            text,
                            xPos + widthStartBars + xStep - (textWidth/2),
                            size.height,
                            textPaint
                        )
                    }

                    val path = Path()
                    path.moveTo(widthStartGuide, 0f)
                    path.lineTo(widthStartGuide, size.height)
                    path.moveTo(widthStartGuide, size.height)
                    path.lineTo(size.width, size.height)
                    drawPath(
                        path = path,
                        color = Color.White.copy(alpha = 0.5f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
            }
        }

    }
}



@Composable
fun EjesGraph(modifier: Modifier, maxValue: Float, minValue: Float) {
    BoxWithConstraints(modifier= modifier) {

        val yStepEjes = maxHeight.value / (maxValue - minValue)

        val spacerBetweenTopAndZero = maxHeight.value - abs(minValue) * yStepEjes
        val spacerBetweenZeroAndBottom = maxHeight.value - spacerBetweenTopAndZero

        Column(
            modifier = Modifier
                .height(maxHeight)
                .wrapContentWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "$$maxValue", fontSize = 16.sp)
            Spacer(Modifier.weight(1f))
//            Spacer(Modifier.weight(spacerBetweenTopAndZero))
//            Text(text = "$0", fontSize = 16.sp)
//            Spacer(Modifier.weight(spacerBetweenZeroAndBottom))
            Text(text = "$$minValue")
        }
    }
}