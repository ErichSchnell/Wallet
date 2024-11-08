package com.example.wallet.presentation.util

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.ui.theme.WalletColors
import kotlin.math.abs

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
                color = WalletColors.icome().onContainer,
                style = Stroke(width = 2.dp.toPx())
            )
            drawPath(
                path = pathTransactionsExpenses,
                color = WalletColors.expenses().onContainer,
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
    GraficoTransacciones(title = "Transactions Acumuladas", transactions = transactionAcumulado)
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
            Text(text = "$title : $maxValue")
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
            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / maxAmount


            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = index * xStep
                val yPos = size.height - (amount * yStep)

                drawLine(
                    color = tr.category.color,
                    start = Offset(xPos + widthStartBars + xStep, size.height),
                    end = Offset(xPos + widthStartBars + xStep, yPos),
                    strokeWidth = xStep * .75f
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
            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / abs(minAmount)

            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = index * xStep
                val yPos = abs((amount * yStep))

                drawLine(
                    color = tr.category.color,
                    start = Offset(xPos + widthStartBars + xStep, 0f),
                    end = Offset(xPos + widthStartBars + xStep, yPos),
                    strokeWidth = xStep * .75f
                )
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
            val width = size.width * 0.85f
            val xStep = width / (transactions.size)
            val yStep = size.height / (maxAmount - minAmount)
            val yZero = size.height - abs(minAmount) * yStep

            val widthStartBars = (size.width - width) / 2f
            val widthStartGuide = (size.width - width) / 2f

            transactions.forEachIndexed { index, tr ->

                val amount = tr.amount.toFloat()
                val xPos = index * xStep
                val yPos = size.height - ((amount - minAmount) * yStep)

                drawLine(
                    color = tr.category.color,
                    start = Offset(xPos + widthStartBars + xStep, yZero),
                    end = Offset(xPos + widthStartBars + xStep, yPos),
                    strokeWidth = xStep * .75f
                )
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

@Composable
fun WalletCategoryGraph(
    modifier: Modifier,
    categorySelected: CategoryUi,
    categories: List<CategoryUi>,
) {
    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val sizeGraph = maxHeight * 0.9f
        val amountTotal = categories[0].amount.toFloat()
        val degreesStep = 360f / amountTotal

        Canvas(modifier = Modifier.size(sizeGraph)) {
            val componentSize = size * 0.8f
            val indicatorStrokeWith = 30f
            var startAngle = 0f
            categories.forEachIndexed { index, category ->

                val sweepAngle = category.amount.toFloat() * degreesStep

                val colorsArc = listOf(category.color.adjustBrightness(0.3f), category.color)
                val stopsArc =
                    List(colorsArc.size) { indexColor -> indexColor / (colorsArc.size - 1).toFloat() * (sweepAngle / 360f) }
                val sweepGradient = Brush.sweepGradient(
                    colorStops = stopsArc.zip(colorsArc).toTypedArray(),
                    center = center
                )

                val radialGradient = Brush.radialGradient(
                    colors = listOf(category.color.copy(alpha = 0.5f), Color.Transparent),
                    center = center,
                )



                if (index != 0) {
                    if (index == 1) {
                        startAngle = 180f - (category.amount.toFloat() * degreesStep / 2f)
                    }

                    rotate(degrees = startAngle) {
                        if (category == categorySelected || categorySelected == categories[0]) {
                            drawArc(
                                size = componentSize,
                                brush = radialGradient,
                                startAngle = 0f,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                topLeft = Offset(
                                    x = (size.width - componentSize.width) / 2f,
                                    y = (size.height - componentSize.height) / 2f
                                )
                            )
                        }

                        drawArc(
                            size = componentSize,
                            brush = sweepGradient,
                            startAngle = 0f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(
                                width = indicatorStrokeWith * if (category == categorySelected) 2.2f else 1.7f,
                                cap = StrokeCap.Butt
                            ),
                            topLeft = Offset(
                                x = (size.width - componentSize.width) / 2f,
                                y = (size.height - componentSize.height) / 2f
                            )
                        )
                    }
                    startAngle += category.amount.toFloat() * degreesStep
                }
            }

        }

        Text(
            text = categorySelected.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun WalletSelectorGraph(
    modifier: Modifier,
    countItems: Int,
    itemSelected: Int,
) {
    Canvas(modifier = modifier) {
        val stepX = size.width * 0.05f
        val xInit = (size.width / 2) - ((countItems / 2) * stepX)

        for (i in 0 until countItems) {
            val xPos = xInit + (i * stepX)

            drawCircle(
                color = Color.White,
                radius = if (itemSelected == i) 8.dp.toPx() else 4.dp.toPx(),
                center = Offset(xPos, 0f)
            )
        }
    }
}