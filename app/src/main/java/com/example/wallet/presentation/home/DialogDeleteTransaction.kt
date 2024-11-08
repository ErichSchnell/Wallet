package com.example.wallet.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wallet.R
import com.example.wallet.presentation.util.DialogWallet

@Composable
fun DialogDeleteTransaction(
    onDismissRequest: () -> Unit,
    onClickAccept: () -> Unit,
) {
    DialogWallet(onDismissRequest = {onDismissRequest()}) {
        Text(text = stringResource(R.string.wishDeleteTransaction))

        Spacer(Modifier.height(32.dp))
        Row {
            Button(onClick = {onDismissRequest()}) {
                Text("Cancel")
            }
            Spacer(Modifier.width(32.dp))
            Button(onClick = {onClickAccept()}) {
                Text("Accept")
            }
        }
    }
}