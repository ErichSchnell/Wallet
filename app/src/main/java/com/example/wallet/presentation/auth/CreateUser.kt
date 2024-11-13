package com.example.wallet.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.util.composables.TextFieldWallet

@Composable
fun CreateUser(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    firstNameValue: String,
    onValueFirstNameChange: (String) -> Unit,
    lastNameValue: String,
    onValueLastNameChange: (String) -> Unit,
    emailValue: String,
    onValueEmailChange: (String) -> Unit,
    passwordValue: String,
    onValuePasswordChange: (String) -> Unit,
    passwordVerifyValue: String,
    onValuePasswordVerifyChange: (String) -> Unit,
    onClickCreateUser: () -> Unit,
    onClickArrowBack: () -> Unit,
) {
    BackgroundCreateUser()

    val focusManager = LocalFocusManager.current


    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

        UpBar(
            Modifier.fillMaxWidth().height(100.dp),
            onClickArrowBack = onClickArrowBack
        )

        Spacer(Modifier.weight(1f))

        FirstName(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            focusManager = focusManager,
            enabled = !isLoading,
            value = firstNameValue,
            onValueChange = onValueFirstNameChange
        )
        Spacer(Modifier.weight(1f))

        LastName(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            focusManager = focusManager,
            enabled = !isLoading,
            value = lastNameValue,
            onValueChange = onValueLastNameChange
        )
        Spacer(Modifier.weight(1f))

        Email(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            focusManager = focusManager,
            enabled = !isLoading,
            value = emailValue,
            onValueChange = onValueEmailChange
        )
        Spacer(Modifier.weight(1f))

        Password(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            focusManager = focusManager,
            enabled = !isLoading,
            value = passwordValue,
            label = stringResource(R.string.password_label_text_field),
            onValueChange = onValuePasswordChange
        )
        Spacer(Modifier.weight(1f))

        Password(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            isVerify = true,
            focusManager = focusManager,
            enabled = !isLoading,
            value = passwordVerifyValue,
            label = stringResource(R.string.verify_password_label_text_field),
            onValueChange = onValuePasswordVerifyChange,
            onClickDone = {
                if (firstNameValue.isNotBlank() &&
                    lastNameValue.isNotBlank() &&
                    emailValue.isNotBlank() &&
                    passwordValue.isNotBlank() &&
                    passwordVerifyValue.isNotBlank()){
                    onClickCreateUser()
                }
            }
        )
        Spacer(Modifier.weight(1f))

        CreateUserButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = onClickCreateUser,
            enabled = (
                firstNameValue.isNotBlank() &&
                lastNameValue.isNotBlank() &&
                emailValue.isNotBlank() &&
                passwordValue.isNotBlank() &&
                passwordVerifyValue.isNotBlank() && !isLoading
            )
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun UpBar(modifier: Modifier, onClickArrowBack: () -> Unit) {
    Box(
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterStart).clickable(onClick = onClickArrowBack).padding(16.dp).size(32.dp),
            painter = painterResource(R.drawable.ic_chevron_left_circle),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.background
        )
//        ArrowBack(
//            modifier = Modifier.align(Alignment.CenterStart),
//            onClick = onClickArrowBack
//        )
        TitleCreteUser(Modifier.align(Alignment.Center))
    }
}

@Composable
fun TitleCreteUser(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.create_user),
        fontWeight = FontWeight.Light,
        fontSize = 34.sp,
        color = MaterialTheme.colorScheme.background
    )
}

@Composable
fun BackgroundCreateUser() {
    Box(Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.singup_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun FirstName(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    focusManager: FocusManager,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldWallet(
        modifier = modifier,
        enabled = enabled,
        label = stringResource(R.string.first_name_label_text_field),
        value = value,
        onValueChange = { onValueChange(it) },
        leadingIconPainterResource = R.drawable.ic_chevron_right,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )
}

@Composable
fun LastName(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    focusManager: FocusManager,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldWallet(
        modifier = modifier,
        enabled = enabled,
        label = stringResource(R.string.last_name_label_text_field),
        value = value,
        onValueChange = { onValueChange(it) },
        leadingIconPainterResource = R.drawable.ic_chevron_right,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )
}

@Composable
fun CreateUserButton(
    modifier: Modifier = Modifier,
    enabled:Boolean,
    onClick:() -> Unit
){
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Black,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
    ) {
        Text(text = stringResource(R.string.create_user))
    }
}
