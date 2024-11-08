package com.example.wallet.presentation.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.EventItem
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.util.TextFieldButtonWallet
import com.example.wallet.presentation.util.TextFieldWallet
import com.example.wallet.presentation.util.TypeOfTransaction
import com.example.wallet.presentation.util.millisToDate
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetWallet(
    maxBottomBardHeight: Dp,
    eventItem: EventItem,
    isLoading: Boolean,
    categories: List<CategoryUi>,
    item: TransactionModelUI? = null,
    onDismissRequest: () -> Unit,
    onClickEvent: (TransactionModelUI) -> Unit,
    onClickAddCategory: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var description by remember { mutableStateOf(item?.description.orEmpty()) }
    var amount by remember { mutableStateOf( if (item?.amount != null) abs(item.amount).toString() else "") }
    var category by remember { mutableStateOf(item?.category ?: CategoryUi()) }
    var budget by remember { mutableStateOf(item?.budget ?: TypeOfTransaction.EMPTY) }
    val isBookmark by remember { mutableStateOf(item?.isBookmark ?: false ) }

    var dateLong by remember { mutableStateOf(item?.date) }
    var showPickerDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    if (showPickerDialog) {
        CalendarPickerDialog(
            onDismissRequest = { showPickerDialog = false },
            onClickConfirmar = {
                dateLong = it
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = {onDismissRequest()},
        sheetState = sheetState,
        containerColor = Color.Black,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(
            modifier = Modifier.height(maxBottomBardHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleNewTransaction()
            HorizontalDivider(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp))

            TypeOfTransactionWallet(
                value = budget,
                enabled = !isLoading,
                onValueChanged = { budget = it }
            )
            Spacer(Modifier.height(16.dp))

            CategoryNewTransaction(
                focusManager = focusManager,
                enabled = !isLoading,
                value = category,
                categories = categories,
                onValueChange = { category = it },
                onClickAddCategory = onClickAddCategory
            )
            Spacer(Modifier.height(16.dp))

            MotiveNewTransaction(
                focusManager = focusManager,
                enabled = !isLoading,
                value = description,
                onValueChange = { description = it }
            )
            Spacer(Modifier.height(16.dp))

            AmountNewTransaction(
                enabled = !isLoading,
                value = amount,
                onValueChange = { amount = it }
            )
            Spacer(Modifier.height(16.dp))

            DateNewTransaction(
                enabled = !isLoading,
                value = dateLong.millisToDate(),
                onClickTextField = { showPickerDialog = true }
            )
            Spacer(Modifier.height(16.dp))

            ButtonAddDeuda(
                txtButton = if (eventItem == EventItem.ADD) R.string.add_transaction else R.string.edit_transaction,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp),
                enabled = !isLoading,
                onClickAdd = {
                    val transaction = TransactionModelUI (
                        id = item?.id.orEmpty(),
                        description = description,
                        amount = amount.ifBlank { "0" }.toDouble(),
                        category = category,
                        budget = budget,
                        isBookmark = isBookmark,
                        date = dateLong
                    )
                    onClickEvent(transaction)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryNewTransaction(
    focusManager: FocusManager,
    enabled: Boolean,
    value: CategoryUi,
    categories: List<CategoryUi>,
    onValueChange: (CategoryUi) -> Unit,
    onClickAddCategory: () -> Unit
) {
    var isExtended by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()){
        TextFieldButtonWallet(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = enabled,
            label = stringResource(R.string.category),
            value = value.name,
            leadingIconPainterResource = R.drawable.ic_chevron_right,
            onClickTextField = {
                focusManager.clearFocus()
                isExtended = true
            }
        )
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
                    categories.forEach { category ->
                        OutlinedButton(
                            border = BorderStroke(1.dp, category.color),
                            onClick = {
                                isExtended = false
                                onValueChange(category)
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ) {
                            Text(category.name)
                        }
                    }
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        onClick = {
                            Log.i("TAG ERICH", "CategoryNewTransaction: click aceptar")
                            onClickAddCategory()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }

                }
            }
        }
    }
}

@Composable
fun TitleNewTransaction(){
    Text(text = stringResource(R.string.title_transaction), fontSize = 16.sp)
}

@Composable
fun TypeOfTransactionWallet(
    value: TypeOfTransaction,
    enabled: Boolean,
    onValueChanged: (TypeOfTransaction) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        RadioButtonWallet(
            name = R.string.title_income,
            enabled = enabled,
            selected = value == TypeOfTransaction.INCOME,
            onClick = { onValueChanged(TypeOfTransaction.INCOME) }
        )

        RadioButtonWallet(
            name = R.string.title_expenses,
            enabled = enabled,
            selected = value == TypeOfTransaction.EXPENSES,
            onClick = { onValueChanged(TypeOfTransaction.EXPENSES) }
        )
    }
    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}

@Composable
fun RadioButtonWallet(
    name: Int,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null, enabled = enabled)
        Spacer(Modifier.width(6.dp))
        Text(stringResource(name))
    }
}

@Composable
fun MotiveNewTransaction(
    enabled: Boolean,
    focusManager: FocusManager,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldWallet(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        enabled = enabled,
        label = stringResource(R.string.description),
        value = value,
        leadingIconPainterResource = R.drawable.ic_chevron_right,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
    )
}

@Composable
fun AmountNewTransaction(
    enabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldWallet(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        enabled = enabled,
        label = stringResource(R.string.amount),
        value = value,
        leadingIconPainterResource = R.drawable.ic_chevron_right,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun DateNewTransaction(
    value: String,
    enabled: Boolean,
    onClickTextField: () -> Unit
) {
    TextFieldButtonWallet(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        label = stringResource(R.string.date),
        value = value,
        enabled = enabled,
        leadingIconPainterResource = R.drawable.ic_calendar,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onClickTextField = onClickTextField
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPickerDialog(
    onDismissRequest: () -> Unit,
    onClickConfirmar: (Long?) -> Unit
) {

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            Text(
                text = "Cancelar",
                modifier = Modifier.clickable { onDismissRequest() }
            )
        },
        confirmButton = {
            Text(
                text = "Confirmar",
                modifier = Modifier.clickable {
                    onClickConfirmar(datePickerState.selectedDateMillis?.plus(72000000))
                    onDismissRequest()
                }
            )
        },
        colors = DatePickerDefaults.colors().copy(

        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ButtonAddDeuda(
    txtButton: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClickAdd: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClickAdd() },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = stringResource(txtButton),
            fontSize = 12.sp,
        )
    }
}