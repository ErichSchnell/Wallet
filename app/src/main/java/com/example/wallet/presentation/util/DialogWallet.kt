package com.example.wallet.presentation.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wallet.R

@Composable
fun DialogWallet(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
){
    Dialog(onDismissRequest = {onDismissRequest()},){
        Box{
            Card(Modifier.padding(top = 34.dp).fillMaxWidth()) {
                Column (
                    modifier = Modifier.padding(top = 68.dp).padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(68.dp),
                painter = painterResource(R.drawable.ic_wallet_circle),
                contentDescription = "logo_cicle",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}