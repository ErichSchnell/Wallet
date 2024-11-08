package com.example.wallet.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.wallet.presentation.util.TextFieldWallet

@Composable
fun LoginUser(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    emailValue: String,
    onValueEmailChange: (String) -> Unit,
    passwordValue: String,
    onValuePasswordChange: (String) -> Unit,
    onClickLogin: () -> Unit,
    onClickLoginWithGoogle: () -> Unit,
    onClickCreateUser: () -> Unit,
) {
    BackgroundLoginUser()

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Spacer(Modifier.weight(1f))
        BannerLogo(Modifier.height(300.dp))

        Spacer(Modifier.weight(1f))
        TitleLogin(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.weight(1f))

        Email(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            enabled = !isLoading,
            focusManager = focusManager,
            value = emailValue,
            onValueChange = onValueEmailChange
        )
        Spacer(Modifier.height(26.dp))

        Password(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            focusManager = focusManager,
            enabled = !isLoading,
            isVerify = true,
            value = passwordValue,
            label = stringResource(R.string.password_label_text_field),
            onValueChange = onValuePasswordChange,
            onClickDone = onClickLogin
        )
        Spacer(Modifier.height(26.dp))

        LoginButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = onClickLogin,
            enabled = (
                    emailValue.isNotBlank() &&
                    passwordValue.isNotBlank() && !isLoading
            )
        )

        Spacer(Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 100.dp))
        Spacer(Modifier.height(12.dp))

        LoginWithRedSocial(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = onClickLoginWithGoogle
        )

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 100.dp))
        Spacer(Modifier.weight(1f))

        TextButtonSingUp(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClickCreateUser
        )
        Spacer(Modifier.height(26.dp))
    }
}

@Composable
fun BackgroundLoginUser() {
    Box(Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun TitleLogin(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.login),
        fontWeight = FontWeight.Light,
        fontSize = 34.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun BannerLogo(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.logo_wallet_card_app),
            contentDescription = ""
        )
    }
}

@Composable
fun Email(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    enabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldWallet(
        modifier = modifier,
        enabled = enabled,
        label = stringResource(R.string.email_label_text_field),
        value = value,
        onValueChange = { onValueChange(it) },
        leadingIconPainterResource = R.drawable.ic_email,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
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
fun Password(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    enabled: Boolean,
    value: String,
    label: String,
    isVerify:Boolean = false,
    onValueChange: (String) -> Unit,
    onClickDone:() -> Unit = {}
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val trailingIcon = if (!isPasswordVisible) {
        R.drawable.ic_eye_on
    } else {
        R.drawable.ic_eye_off
    }


    TextFieldWallet(
        modifier = modifier,
        enabled = enabled,
        label = label,
        value = value,
        onValueChange = { onValueChange(it) },
        isTextVisible = isPasswordVisible,
        leadingIconPainterResource = R.drawable.ic_lock,
        trailingIconPainterResource = trailingIcon,
        onClickTrailingIcon = { isPasswordVisible = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
            imeAction = if (isVerify) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {focusManager.moveFocus(FocusDirection.Down)},
            onDone = {
                focusManager.clearFocus()
                onClickDone()
            }
        )
    )
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick:() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Black,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { onClick() }
    ) {
        Text(text = stringResource(R.string.login))
    }
}

@Composable
fun LoginWithRedSocial(
    modifier: Modifier = Modifier,
    onClick:() -> Unit
){
    Row (modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Box(
            Modifier
                .background(Color.Black, CircleShape)
                .size(50.dp)
                .clickable { onClick() }
                .padding(8.dp)
            ,
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = ""
            )
        }

    }

}

@Composable
fun TextButtonSingUp(
    modifier: Modifier = Modifier,
    onClick:() -> Unit
){
    TextButton(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.textButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text( text = stringResource(R.string.text_without_acount))
    }
}