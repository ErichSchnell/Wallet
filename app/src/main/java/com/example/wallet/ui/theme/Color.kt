package com.example.wallet.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.wallet.presentation.util.adjustBrightness

val HomeContainer = Color(0xFF21455a)
val OnHomeContainer = Color(0xFF83b5d3)

val IncomeContainer = Color(0xFF1B4B1E)
val OnIncomeContainer = Color(0xFF5AC462)

val ExpensesContainer = Color(0xFF652525)
val OnExpensesContainer = Color(0xFFd89293)

val Surface = Color(0xFF252525)
val SurfaceVariant = Color(0xFF252525)
val Background = Color(0xFF252525)
val OnBackground = Color(0xFFD9D9D9)


val ColorOption1 = Color(0xFF59AABE)
val ColorOption2 = Color(0xFF59BE59)
val ColorOption3 = Color(0xFF59BE77)
val ColorOption4 = Color(0xFF59BE96)
val ColorOption5 = Color(0xFF59BEB6)
val ColorOption6 = Color(0xFF59BE81)
val ColorOption7 = Color(0xFF5977BE)
val ColorOption8 = Color(0xFF5983BE)
val ColorOption9 = Color(0xFF5969BE)
val ColorOption10 = Color(0xFF6859BE)
val ColorOption11 = Color(0xFF6F59BE)
val ColorOption12 = Color(0xFF7C59BE)
val ColorOption13 = Color(0xFF9C59BE)
val ColorOption14 = Color(0xFFBE5959)
val ColorOption15 = Color(0xFFBE5C59)
val ColorOption16 = Color(0xFFBE597C)
val ColorOption17 = Color(0xFFBE5981)
val ColorOption18 = Color(0xFFBE59A4)
val ColorOption19 = Color(0xFFBE7C59)
val ColorOption20 = Color(0xFFBE8359)
val ColorOption21 = Color(0xFFBE9C59)
val ColorOption22 = Color(0xFFBE9C83)
val ColorOption23 = Color(0xFFBEB659)
val ColorOption24 = Color(0xFFAABE59)
val ColorOption25 = Color(0xFF94BE59)
val ColorOption26 = Color(0xFF77BE59)
val ColorOption27 = Color(0xFFB459BE)

val categoryColors = listOf(
    ColorOption1,
    ColorOption2,
    ColorOption3,
    ColorOption4,
    ColorOption5,
    ColorOption6,
    ColorOption7,
    ColorOption8,
    ColorOption9,
    ColorOption10,
    ColorOption11,
    ColorOption12,
    ColorOption13,
    ColorOption14,
    ColorOption15,
    ColorOption16,
    ColorOption17,
    ColorOption18,
    ColorOption19,
    ColorOption20,
    ColorOption21,
    ColorOption22,
    ColorOption23,
    ColorOption24,
    ColorOption25,
    ColorOption26,
    ColorOption27,
)

data class WalletColors(
    val container: Color = HomeContainer,
    val onContainer: Color = OnHomeContainer
) {
    companion object {
        fun icome() = WalletColors(
            container = IncomeContainer,
            onContainer = OnIncomeContainer
        )

        fun expenses() = WalletColors(
            container = ExpensesContainer,
            onContainer = OnExpensesContainer,
        )
    }

}
