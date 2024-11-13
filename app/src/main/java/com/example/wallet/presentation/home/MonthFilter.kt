package com.example.wallet.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R

@Composable
fun MonthFilter(
    date: String,
    onClickPreviousMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        PreviousMonth(onClickPreviousMonth)
        MonthTitle(date)
        NextMonth(onClickNextMonth)
    }
}
@Composable
fun PreviousMonth(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(painter = painterResource(R.drawable.ic_chevron_left_circle), contentDescription = "")
    }
}

@Composable
fun NextMonth(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(painter = painterResource(R.drawable.ic_chevron_right_circle), contentDescription = "")
    }
}

@Composable
fun MonthTitle(date:String) {
    Text(date, fontSize = 24.sp)
}

