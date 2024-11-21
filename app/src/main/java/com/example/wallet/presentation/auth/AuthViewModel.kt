package com.example.wallet.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.data.network.AuthService
import com.example.wallet.data.network.DatabaseService
import com.example.wallet.data.response.toData
import com.example.wallet.presentation.model.ProfileModelUi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AuthService,
    private val db: DatabaseService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState

    fun isUserLogged(navigateToHome: (String) -> Unit) {
        auth.getUserLogged()?.let { user ->
            user.email?.let {
                navigateToHome(it)
            }
        }
    }

    fun login(email: String, password: String, navigateToHome: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = withContext(Dispatchers.IO) { auth.login(email, password) }

                user?.email?.let {
                    navigateToHome(it)
                }

            } catch (e: FirebaseAuthException) {
                _uiState.update { it.copy(toastText = "${e.message}") }
                Log.e("TAG", "login: ${e.message}")
                Log.e("TAG", "login: ${e.errorCode}")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    fun singUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordVerify: String,
        navigateToHome: (String) -> Unit
    ) {

        if (password != passwordVerify) {
            _uiState.update { it.copy(toastText = "Las contraseñas deben coincidir") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val singUpUserResult = withContext(Dispatchers.IO) { singUpUser(email = email, password = password) }

            if (singUpUserResult != null){
                val setUserResult = withContext(Dispatchers.IO) { setUser(email = singUpUserResult, firstName = firstName, lastName = lastName ) }

                val setProfileResult = withContext(Dispatchers.IO) { setProfile(email = singUpUserResult, profile = ProfileModelUi()) }

                if (setUserResult && setProfileResult){
                    navigateToHome(singUpUserResult)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    suspend private fun singUpUser(email: String, password: String): String? {
        try {
            return auth.register(email, password)?.email
        } catch (e: FirebaseAuthException) {
            _uiState.update { it.copy(toastText = "${e.message}") }
            Log.e("TAG", "singUp: ${e.message}")
            Log.e("TAG", "singUp: ${e.errorCode}")

        }
        return null
    }
    private suspend fun setProfile(email: String, profile: ProfileModelUi): Boolean {
        try {
            return db.setProfile(email, profile.toData())
        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastText = "${e.message}") }
            Log.e("TAG", "getProfile: ${e.message}")
            Log.e("TAG", "getProfile: ${e.code}")
        }
        return false
    }
    private suspend fun setUser(email: String, firstName: String, lastName: String): Boolean {
        try {
            return  db.setUser(firstName = firstName, lastName = lastName, email = email)
        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastText = "${e.message}") }
            Log.e("TAG", "setUser: ${e.message}")
            Log.e("TAG", "setUser: ${e.code}")
        }
        return false
    }


    /*
    *------- Google ---------
    */
    fun onGoogleLoginSelected(googleLauncherLogin: (GoogleSignInClient) -> Unit) {
        val gsc = auth.getGoogleClient()
        googleLauncherLogin(gsc)
    }
    fun loginWithGoogle(idToken: String, navigateToHome: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = withContext(Dispatchers.IO) { auth.loginWithGoogle(idToken) }
                Log.i("TAG ERICH", "loginWithGoogle user: ${user}")
                user?.let {
                    val isUser = getUser(it.email.orEmpty())
                    Log.i("TAG ERICH", "loginWithGoogle isUser: ${user}")
                    if (isUser){
                        navigateToHome(it.email.orEmpty())
                    } else {
                        val setUserResult = withContext(Dispatchers.IO) { setUser(email = it.email.orEmpty(), firstName = it.displayName.orEmpty(), lastName = "" ) }
                        Log.i("TAG ERICH", "loginWithGoogle setUserResult: ${setUserResult}")
                        val setProfileResult = withContext(Dispatchers.IO) { setProfile(email = it.email.orEmpty(), profile = ProfileModelUi()) }
                        Log.i("TAG ERICH", "loginWithGoogle setProfileResult: ${setProfileResult}")
                        if (setUserResult && setProfileResult){
                            navigateToHome(it.email.orEmpty())
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("TAG ERICH", "${e.message}")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    private suspend fun getUser(userEmail: String): Boolean {
        try {
            val user = db.getUser(userEmail)
            user.email.ifEmpty { return false }
        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastText = "${e.message}") }
            return false
        }
        return true
    }


    fun showToast(toast: String) {
        _uiState.update { it.copy(toastText = toast) }
    }
    fun clearToast() {
        _uiState.update { it.copy(toastText = "") }
    }
    fun selectScreen(screen: ScreeView) {
        _uiState.update { it.copy(screen = screen) }
    }
}

data class LoginUIState(
    val isLoading: Boolean = false,
    val screen: ScreeView = ScreeView.LOGIN,
    val toastText: String = "",
)

sealed class ScreeView {
    object LOGIN : ScreeView()
    object SING_UP : ScreeView()
}


sealed class FirebaseExeptions(val error: String) {
    object EMAIL_ALREADY_USE : FirebaseExeptions("ERROR_EMAIL_ALREADY_IN_USE")
    object WEAK_PASSWORD : FirebaseExeptions("WEAK_PASSWORD")
    object INVALID_EMAIL : FirebaseExeptions("INVALID_EMAIL")
    object EMAIL3_ALREADY_USE : FirebaseExeptions("ERROR_EMAIL_ALREADY_IN_USE")
    object EMAIL4_ALREADY_USE : FirebaseExeptions("ERROR_EMAIL_ALREADY_IN_USE")
    object EMAIL5_ALREADY_USE : FirebaseExeptions("ERROR_EMAIL_ALREADY_IN_USE")
}