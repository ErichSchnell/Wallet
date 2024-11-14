package com.example.wallet.presentation.util.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldDefaults.colorsWallet() = TextFieldDefaults.colors().copy(
    cursorColor = MaterialTheme.colorScheme.secondary,
    unfocusedTextColor = MaterialTheme.colorScheme.background,
    focusedTextColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
    focusedContainerColor = MaterialTheme.colorScheme.onBackground,
    unfocusedLabelColor = MaterialTheme.colorScheme.background,
    focusedLabelColor = MaterialTheme.colorScheme.background,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.background,
    focusedTrailingIconColor = MaterialTheme.colorScheme.background,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.background,
    focusedLeadingIconColor = MaterialTheme.colorScheme.background,
    textSelectionColors = TextSelectionColors(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
    ),

//    disabledTextColor = MaterialTheme.colorScheme.background,
//    disabledContainerColor = MaterialTheme.colorScheme.onBackground,
//    disabledLabelColor = MaterialTheme.colorScheme.background,
//    disabledTrailingIconColor = MaterialTheme.colorScheme.background,
//    disabledLeadingIconColor = MaterialTheme.colorScheme.background,
)


@Composable
fun TextFieldWallet(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    enabled: Boolean = true,
    isTextVisible: Boolean = true,
    leadingIconPainterResource: Int? = null,
    trailingIconPainterResource: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    onClickTrailingIcon: (Boolean) -> Unit = {},
) {
    var value by remember { mutableStateOf(TextFieldValue(text)) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        Log.i("TAG ERICH", "TextFieldWallet ${value.text}: $isFocused")
        value = value.copy(selection = if (isFocused) TextRange(0, value.text.length) else TextRange.Zero)
    }

    TextField(
        modifier = modifier,
        label = { Text(text = label) },
        value = value,
        enabled = enabled,
        onValueChange = {
            value = it
            onValueChange(it.text) },
        colors = TextFieldDefaults.colorsWallet(),
        interactionSource = interactionSource,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        leadingIcon = {
            if (leadingIconPainterResource != null) {
                Icon(
                    painter = painterResource(leadingIconPainterResource),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        visualTransformation = if (isTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (trailingIconPainterResource != null) {
                IconButton(onClick = { onClickTrailingIcon(!isTextVisible) }) {
                    Icon(
                        painter = painterResource(trailingIconPainterResource),
                        contentDescription = null,
                    )
                }
            }
        },
    )
}



@Composable
fun TextFieldButtonWallet(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    enabled: Boolean,
    isTextVisible: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    leadingIconPainterResource: Int? = null,
    trailingIconPainterResource: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClickTextField: () -> Unit = {}
) = TextField(
    modifier = modifier.clickable(enabled = enabled) { onClickTextField() },
//    if(enabled) {
//        modifier.clickable { onClickTextField() }
//    } else {
//        modifier
//    },
    label = { Text(text = label) },
    value = value,
    enabled = false,
    onValueChange = {},
    colors = TextFieldDefaults.colorsWallet().copy(
        disabledTextColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = backgroundColor,
        disabledLabelColor = MaterialTheme.colorScheme.background,
        disabledTrailingIconColor = MaterialTheme.colorScheme.background,
        disabledLeadingIconColor = MaterialTheme.colorScheme.background,
    ),
    maxLines = 1,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    singleLine = true,
    leadingIcon = {
        if (leadingIconPainterResource != null){
            Icon(
                painter = painterResource(leadingIconPainterResource),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    },
    visualTransformation = if (isTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
    trailingIcon = {
        if (trailingIconPainterResource != null){
            Icon(
                painter = painterResource(trailingIconPainterResource),
                contentDescription = null,
            )
        }
    },
)
