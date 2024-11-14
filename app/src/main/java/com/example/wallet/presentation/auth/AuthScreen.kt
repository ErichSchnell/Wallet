package com.example.wallet.presentation.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wallet.presentation.util.ShowToast


@Composable
fun AuthScreen (
    authViewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: (String) -> Unit
) {

    LaunchedEffect(true) { authViewModel.isUserLogged(navigateToHome) }

    val uiState by authViewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }        //Erich
    var lastName by remember { mutableStateOf("") }         //Schnell
    var email by remember { mutableStateOf("") }            //schnellerich@hotmail.com
    var password by remember { mutableStateOf("") }         //123456
    var passwordVerify by remember { mutableStateOf("") }   //123456



    Scaffold { paddingValues ->
        when(uiState.screen){
            ScreeView.LOGIN -> {
                LoginUser(
                    paddingValues = paddingValues,
                    isLoading = uiState.isLoading,
                    emailValue = email,
                    onValueEmailChange = { email = it },
                    passwordValue = password,
                    onValuePasswordChange = { password = it },
                    onClickLogin = { authViewModel.login(email, password, navigateToHome) },
                    onClickLoginWithGoogle = { authViewModel.showToast("Login With Google") },
                    onClickCreateUser = { authViewModel.selectScreen(ScreeView.SING_UP) },
                )
            }
            ScreeView.SING_UP -> {
                CreateUser(
                    paddingValues = paddingValues,
                    isLoading = uiState.isLoading,
                    firstNameValue = firstName,
                    onValueFirstNameChange = { firstName = it },
                    lastNameValue = lastName,
                    onValueLastNameChange = { lastName = it },
                    emailValue = email,
                    onValueEmailChange = { email = it },
                    passwordValue = password,
                    onValuePasswordChange = { password = it },
                    passwordVerifyValue = passwordVerify,
                    onValuePasswordVerifyChange = { passwordVerify = it },
                    onClickCreateUser = {
                        authViewModel.singUp(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            passwordVerify = passwordVerify,
                            navigateToHome = navigateToHome
                        )
                    },
                    onClickArrowBack = {
                        authViewModel.selectScreen(ScreeView.LOGIN)
                    }
                )
            }
        }
    }

    Loading(uiState.isLoading)

    ShowToast(uiState.toastText){
        authViewModel.clearToast()
    }

    BackHandler(uiState.screen == ScreeView.SING_UP) {
        authViewModel.selectScreen(ScreeView.LOGIN)
    }

}

@Composable
fun Loading(loading: Boolean) {
    if (!loading) return

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun PreviewLogin(){
    //AuthScreen()
}