package com.example.wallet.data.response

import android.util.Log
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.TypeOfTransaction
import com.example.wallet.presentation.util.ex.toTimeStamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import kotlin.math.abs

data class TransactionResponseModelData (
    val id:String? = null,
    val motive:String? = null,
    val amount:Double? = null,
    val category:String? = null,
    val budget:Int? = null,
    val isBookmark:Boolean? = null,
    val date: Timestamp? = null,
)

fun DocumentSnapshot.DTO(): TransactionResponseModelData? {
    return try {
        val date = TransactionResponseModelData(
            id = getString("id"),
            motive = getString("motive"),
            amount = getDouble("amount"),
            category = getString("category"),
            budget = getLong("budget")?.toInt(),  // Convertir Long a Int si es necesario
            date = getTimestamp("date"),
            isBookmark = getBoolean("isBookmark") ?: false  // Valor por defectok
        )
        date
    } catch (e: Exception) {
        Log.e("Firestore", "Error deserializing document: ${e.message}")
        null
    }
}

fun TransactionModelUI.toData(): TransactionResponseModelData {
    Log.i("TAG ERICH", "toData: $this")
    if (this.budget == TypeOfTransaction.EMPTY) throw IllegalArgumentException("0")
    if (this.category.id.isBlank()) throw IllegalArgumentException("3")
    if (this.description.isBlank()) throw IllegalArgumentException("1")
    if (this.amount == .0) throw IllegalArgumentException("2")

    val timeStamp = this.date.toTimeStamp()
    val idCurrent = this.id.ifBlank {
        getCustomId()
    }

    return try {
        TransactionResponseModelData(
            id = idCurrent,
            motive = this.description,
            amount = abs(this.amount),
            category = this.category.id,
            budget = this.budget.state,
            isBookmark = this.isBookmark,
            date = timeStamp
        )
    } catch (e: java.lang.Exception) {
        throw IllegalArgumentException("3")
    }
}

private fun getCustomId(): String{
    return Date().time.toString()
}