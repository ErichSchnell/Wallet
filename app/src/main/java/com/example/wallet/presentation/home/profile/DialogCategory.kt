package com.example.wallet.presentation.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wallet.R
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.util.DialogWallet
import com.example.wallet.presentation.util.TextFieldButtonWallet
import com.example.wallet.presentation.util.TextFieldWallet
import com.example.wallet.ui.theme.categoryColors


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogAddCategory(
    showAddCategory: Boolean,
    category: CategoryUi? = null,
    onDismissRequest: () -> Unit,
    onClickAddCategory: (CategoryUi) -> Unit
) {
    if (!showAddCategory) return

    var isExtended by remember { mutableStateOf(false) }

    val ct = category ?: CategoryUi()
    var categoryId by remember { mutableStateOf(ct.id) }
    var categoryName by remember { mutableStateOf(ct.name) }
    var categoryColor by remember { mutableStateOf(ct.color) }

    var enabled by remember { mutableStateOf(true) }


    DialogWallet(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldWallet(
                label = stringResource(R.string.category),
                value = categoryName,
                onValueChange = { categoryName = it },
                leadingIconPainterResource = R.drawable.ic_chevron_right,
            )
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextFieldButtonWallet(
                    backgroundColor = categoryColor,
                    enabled = enabled,
                    label = stringResource(R.string.color),
                    isTextVisible = false,
                    value = "",
                    leadingIconPainterResource = R.drawable.ic_chevron_right,
                    onClickTextField = {
                        isExtended = true
                    }
                )
                Box(
                    Modifier
                        .clickable { isExtended = true }
                        .padding(16.dp)
                        .size(24.dp)
                        .background(categoryColor))
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onDismissRequest = { isExtended = false },
                    expanded = isExtended,
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            categoryColors.forEach { itemColor ->
                                Box(
                                    Modifier
                                        .clickable {
                                            categoryColor = itemColor
                                            isExtended = false
                                        }
                                        .padding(16.dp)
                                        .size(24.dp)
                                        .background(itemColor)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))


            Row {
                Button(onClick = { onDismissRequest() }) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(32.dp))
                Button(
                    enabled = categoryName.isNotBlank(),
                    onClick = {
                        onClickAddCategory(
                            CategoryUi(
                                id = ct.id,
                                name = categoryName,
                                color = categoryColor
                            )
                        )
                    }) {
                    Text("Accept")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun DialogDeleteCategory(
    category: CategoryUi?,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onClickAccept: () -> Unit,
) {
    category ?: return
    if (!showDialog) return

    DialogWallet(onDismissRequest = {onDismissRequest()}) {
        Text(text = stringResource(R.string.wishDeleteCategory))
        Spacer(Modifier.height(32.dp))

        Text(text = category.name, color = category.color)
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