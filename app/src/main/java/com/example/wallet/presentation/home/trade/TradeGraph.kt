package com.example.wallet.presentation.home.trade

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.util.composables.CardWallet
import com.example.wallet.presentation.util.ex.adjustBrightness
import com.example.wallet.presentation.util.ex.getDay
import com.example.wallet.presentation.util.ex.getDayOfMonth
import com.example.wallet.presentation.util.ex.getWeek
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun GraficoByWeek(
    title: String,
    transactions: List<TransactionModelUI>,
) {
    val tr = transactions.map { it.copy(amount = abs(it.amount)) }
    val week1 = tr.getWeek(1)
    val week2 = tr.getWeek(2)
    val week3 = tr.getWeek(3)
    val week4 = tr.getWeek(4)

    val weeks = listOf(week1,week2,week3,week4)
    val weeksAmount = listOf(week1.sumOf { it.amount },week2.sumOf { it.amount },week3.sumOf { it.amount },week4.sumOf { it.amount })

    val maxAmount = weeksAmount.max().toFloat()

    CardWallet(height = 300.dp) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)

            Row(
                Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .fillMaxWidth()) {
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

                    weeks.forEachIndexed { index, week ->

                        val xPos = index * xStep
                        var yPre = size.height

                        week.forEach {  tr ->
                            val amount = tr.amount.toFloat()
                            val yPos = yPre - (amount * yStep)

                            drawLine(
                                color = tr.category.color,
                                start = Offset(xPos + widthStartBars + xStep, yPre),
                                end = Offset(xPos + widthStartBars + xStep, yPos),
                                strokeWidth = xStep * .75f
                            )

                            yPre = yPos
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
fun GraficoByDay(
    title: String,
    transactions: List<TransactionModelUI>,
) {
    val tr = transactions.map { it.copy(amount = abs(it.amount)) }

    val days = mutableListOf<List<TransactionModelUI>>()
    val daysAmount = mutableListOf<Double>()
    val lastDayOfMonth = tr.last().date.getDayOfMonth() ?: 31
    for (i in 1 .. lastDayOfMonth){
        val day = tr.getDay(i)
        if (day.isNotEmpty()){
            days.add(day)
            daysAmount.add(day.sumOf { it.amount })
        }
    }

    val maxAmount = daysAmount.max().toFloat()

    CardWallet(height = 300.dp) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)

            Row(
                Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .fillMaxWidth()) {
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
                    val xStep = width / days.count()
                    val yStep = size.height / maxAmount


                    val widthStartBars = (size.width - width) / 2f
                    val widthStartGuide = (size.width - width) / 2f

                    days.forEachIndexed { index, day ->
                        val xPos = index * xStep
                        var yPre = size.height
                        day.forEach {  tr ->
                            val amount = tr.amount.toFloat()
                            val yPos = yPre - (amount * yStep)

                            drawLine(
                                color = tr.category.color,
                                start = Offset(xPos + widthStartBars + xStep, yPre),
                                end = Offset(xPos + widthStartBars + xStep, yPos),
                                strokeWidth = xStep * .75f
                            )

                            yPre = yPos
                        }


                        val textPaint = TextPaint().apply {
                            color = Color.White.toArgb() // Color del texto
                            textSize = calculateTextSize(days.count(), 8.sp.toPx(), 12.sp.toPx())      // Tamaño del texto
                            isAntiAlias = true           // Para bordes más suaves
                        }
                        val text = "${day.first().date.getDayOfMonth()}"
                        val textWidth = textPaint.measureText(text)
                        drawContext.canvas.nativeCanvas.drawText(
                            text,
                            xPos + widthStartBars + xStep - (textWidth / 2),
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



fun calculateTextSize(dataCount: Int, maxTextSize: Float, minTextSize: Float): Float {
    val scale = 1f - (dataCount / 100f)
    return max(minTextSize, min(maxTextSize, maxTextSize * scale))
}