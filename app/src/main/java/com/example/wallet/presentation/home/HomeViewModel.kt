package com.example.wallet.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.R
import com.example.wallet.data.network.AuthService
import com.example.wallet.data.network.DatabaseService
import com.example.wallet.data.response.TransactionResponseModelData
import com.example.wallet.data.response.toData
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.DetailsScreen
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.UserModelUI
import com.example.wallet.presentation.util.getMonthAndYearString
import com.example.wallet.presentation.util.getProfileRemoveCategory
import com.example.wallet.presentation.util.getProfileWithNewCategory
import com.example.wallet.presentation.util.nextMonht
import com.example.wallet.presentation.util.previousMonht
import com.example.wallet.presentation.util.setTransactionWithCategory
import com.example.wallet.presentation.util.updateDate
import com.example.wallet.presentation.util.updateTransactions
import com.example.wallet.ui.theme.WalletColors
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: AuthService,
    private val db: DatabaseService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState


    fun getAccount(userEmail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update { it.copy(isLoading = true) }
                getUser(userEmail)
                getProfiles()
                getTransactions()
            }
        }
    }

    private suspend fun getUser(userEmail: String) {
        try {
            val response = db.getUser(userEmail)
            _uiState.update { it.copy(user = response) }
        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastTextExeption = "${e.message}") }
        }
    }

    private suspend fun getProfiles(newProfileSelected:ProfileModelUi? = null) {
        try {
            val response = db.getProfiles(_uiState.value.user.email)

            val profileSelected = response.find { profile -> profile == newProfileSelected }

            _uiState.update { it.copy(profiles = response, profileSelected = profileSelected ?: response[0]) }

        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastTextExeption = "${e.message}") }
        }
    }

    private suspend fun getTransactions() {
        try {
            db.getTransactions(
                email = _uiState.value.user.email,
                profileId = _uiState.value.profileSelected?.id.orEmpty()
            ).collect { transactions ->
                _uiState.setTransactionWithCategory(transactions)
                _uiState.updateDate()
                _uiState.updateTransactions()
                _uiState.update { it.copy(isLoading = false) }
            }
        } catch (e: FirebaseFirestoreException) {
            _uiState.update { it.copy(toastTextExeption = "${e.message}") }
            Log.e("TAG", "getTransactions: ${e.message}")
            Log.e("TAG", "getTransactions: ${e.code}")
        }
    }

    fun addTransaction(transactionItem: TransactionModelUI) {
        val dto: TransactionResponseModelData
        try {
            dto = transactionItem.toData()
        } catch (e:Exception){
            when(e.message?.toInt()){
                0 -> {_uiState.update { it.copy(toastText = R.string.complete_type_of_transaction) }}
                1 -> {_uiState.update { it.copy(toastText = R.string.complete_description) }}
                2 -> {_uiState.update { it.copy(toastText = R.string.complete_amount) }}
                3 -> {_uiState.update { it.copy(toastText = R.string.complete_category) }}
                else -> {_uiState.update { it.copy(toastText = R.string.try_again) }}
            }
            return
        }

        _uiState.update { it.copy(transactionSelected = null) }

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.setTransaction(
                    email = _uiState.value.user.email,
                    profileId = _uiState.value.profileSelected?.id ?: "default",
                    transaction = dto
                )
            }
        }
    }

    fun logout(navigateToLogin: () -> Unit) {
        auth.logout()
        navigateToLogin()
    }

    fun clearToastExeption() {
        _uiState.update { it.copy(toastTextExeption = "") }
    }
    fun clearToast() {
        _uiState.update { it.copy(toastText = R.string.empty) }
    }

    fun selectItemTransaction(item: TransactionModelUI?) {
        _uiState.update { it.copy(transactionSelected = item) }
    }
    fun removeItem(itemId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.removeTransaction(
                    email = _uiState.value.user.email,
                    profileId = _uiState.value.profileSelected?.id.orEmpty(),
                    itemId = itemId
                )
            }
        }
    }

    fun navigateToForId(id: Int) {
        _uiState.update { it.copy(screens = getScreen(id)) }
    }

    private fun getScreen(id: Int): Screens {
         val date = when (id) {
            Screens.INCOME.id -> Screens.INCOME
            Screens.OVERVIEW.id -> Screens.OVERVIEW
            Screens.EXPENSES.id -> Screens.EXPENSES
            Screens.SETTING.id -> Screens.SETTING
            else -> Screens.OVERVIEW
        }
        return date
    }


    private suspend fun setProfile(email: String, profile: ProfileModelUi): Boolean {
        try {
            return db.setProfile(email, profile.toData())
        } catch (e: Exception) {
            _uiState.update { it.copy(toastTextExeption = "${e.message}") }
            Log.e("TAG", "getProfile: ${e.message}")
//            Log.e("TAG", "getProfile: ${e.code}")
        }
        return false
    }

    fun changeProfile(profile: ProfileModelUi?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (profile == null){
                    _uiState.update { it.copy(isLoading = true) }
                    val nextProfile = "perfil_${getNextProfileNumber()}"
                    val newProfile = ProfileModelUi(name = nextProfile)
                    val result = setProfile(_uiState.value.user.email, newProfile)

                    if (result){ getProfiles(newProfile) }
                } else {
                    _uiState.update { it.copy(profileSelected = profile) }
                }
                getTransactions()
            }
        }
    }
    private fun getNextProfileNumber():Int{
        return _uiState.value.profiles.count().inc()
    }

    fun addCategory(category: CategoryUi) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){

                _uiState.update { it.copy(isLoading = true) }
                val profile = _uiState.getProfileWithNewCategory(category)
                if (profile != null){

                    val result = setProfile(_uiState.value.user.email, profile)

                    if (result){
                        getProfiles(profile)
//                        _uiState.updateTransactions(_uiState.value.allTransactions)
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun changeCategory(category: CategoryUi) {
        when(_uiState.value.screens){
            Screens.INCOME -> { _uiState.update { it.copy(incomes = it.incomes.copy(categorySelected = category)) }}
            Screens.EXPENSES -> { _uiState.update { it.copy(expenses = it.expenses.copy(categorySelected = category)) }}
            Screens.OVERVIEW -> { _uiState.update { it.copy(home = it.home.copy(categorySelected = category)) }}
            else -> {}
        }
        _uiState.updateTransactions()
    }

    fun deleteCategory(category: CategoryUi) {
        val aux = _uiState.value.allTransactions.find { it.category.id == category.id }
        if (aux != null){
            _uiState.update { it.copy(toastText = R.string.category_usada) }
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO){

                _uiState.update { it.copy(isLoading = true) }
                val profile = _uiState.getProfileRemoveCategory(category)
                if (profile != null){

                    val result = setProfile(_uiState.value.user.email, profile)

                    if (result){
                        getProfiles(profile)
//                        _uiState.updateTransactions()
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun previousMonth() {
        val index = _uiState.value.monthList.indexOf(_uiState.value.dateSelected)

        when{
            index == 0 -> {
                _uiState.update { it.copy(toastText = R.string.limit_here) }
            }
            index > 0 -> {
                _uiState.update { it.copy( dateSelected = _uiState.value.monthList[index - 1]) }
                _uiState.updateTransactions()
            }
            else -> {
                _uiState.update { it.copy( dateSelected = _uiState.value.monthList.last()) }
                _uiState.updateTransactions()
            }
        }
    }

    fun nextMonth() {
        val index = _uiState.value.monthList.indexOf(_uiState.value.dateSelected)

        when{
            index == (uiState.value.monthList.size - 1) -> {
                _uiState.update { it.copy(toastText = R.string.limit_here) }
            }
            index < (uiState.value.monthList.size - 1) -> {
                _uiState.update { it.copy( dateSelected = _uiState.value.monthList[index + 1]) }
                _uiState.updateTransactions()
            }
            else -> {
                _uiState.update { it.copy( dateSelected = _uiState.value.monthList.last()) }
                _uiState.updateTransactions()
            }
        }
    }
}

data class HomeUIState(
    val isLoading: Boolean = true,

    val screens: Screens = Screens.OVERVIEW,

    val dateSelected: String? = null,
    val monthList: List<String> = emptyList(),

    val user: UserModelUI = UserModelUI(),
    val profiles: List<ProfileModelUi> = emptyList(),

    val profileSelected: ProfileModelUi? = null,

    val allTransactions: List<TransactionModelUI> = emptyList(),
    val home: DetailsScreen = DetailsScreen(),
    val incomes: DetailsScreen = DetailsScreen(),
    val expenses: DetailsScreen = DetailsScreen(),

    val transactionSelected:TransactionModelUI? = null,
    val showNewTransaction: Boolean = false,

    val toastTextExeption: String = "",
    val toastText: Int = R.string.empty,
)

sealed class Screens(
    val id: Int,
    val title: Int,
    val iconNavigation: Int,
    val iconUnFocused: Int,
    val iconFocused: Int,
    val colors: WalletColors = WalletColors(),
    val bg: Int
) {

    data object INCOME : Screens(
        id = 0,
        title = R.string.title_income,
        iconNavigation = R.drawable.ic_credit_card_plus,
        iconUnFocused = R.drawable.ic_credit_card_plus_outline,
        iconFocused = R.drawable.ic_credit_card_plus,
        colors = WalletColors.icome(),
        bg = R.drawable.income_bg_variant
    )

    data object OVERVIEW : Screens(
        id = 1,
        title = R.string.title_home,
        iconNavigation = R.drawable.ic_wallet_circle,
        iconUnFocused = R.drawable.ic_wallet_circle_outline,
        iconFocused = R.drawable.ic_wallet_circle,
        colors = WalletColors(),
        bg = R.drawable.home_bg_variant
    )

    data object EXPENSES : Screens(
        id = 2,
        title = R.string.title_expenses,
        iconNavigation = R.drawable.ic_credit_card_minus,
        iconUnFocused = R.drawable.ic_credit_card_minus_outline,
        iconFocused = R.drawable.ic_credit_card_minus,
        colors = WalletColors.expenses(),
        bg = R.drawable.expenses_bg_variant
    )

    data object SETTING : Screens(
        id = 3,
        title = R.string.title_setting,
        iconNavigation = R.drawable.ic_chevron_left_circle,
        iconUnFocused = R.drawable.ic_chevron_left_circle,
        iconFocused = R.drawable.ic_chevron_left_circle,
        bg = R.drawable.new_transaction_bg
    )
}