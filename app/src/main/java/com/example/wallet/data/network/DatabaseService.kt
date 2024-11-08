package com.example.wallet.data.network

import android.util.Log
import com.example.wallet.data.response.DTO
import com.example.wallet.data.response.ProfileResponseModelData
import com.example.wallet.data.response.TransactionResponseModelData
import com.example.wallet.data.response.UserResponseModelData
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.UserModelUI
import com.example.wallet.presentation.model.toUi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseService @Inject constructor(private val db: FirebaseFirestore) {

    companion object {
        const val USER_PATH = "users"
        const val PROFILE_PATH = "profile"
        const val TRANSACTION_PATH = "transactions"
        const val TRANSACTION_DATE_PATH = "date"
    }


    /*
    ---------------------------- USER ------------------------------------
     */
    suspend fun setUser(email: String, firstName: String, lastName: String): Boolean {
        val user = hashMapOf(
            "firstname" to firstName,
            "lastname" to lastName,
            "email" to email,
        )
        try {
            db.collection(USER_PATH).document(email).set(user).await()
            return true
        } catch (e: Exception) {
            Log.e("TAG", "Dataservice setUser: ${e.message}", e)
            throw e
        }
    }

    suspend fun getUser(email: String): UserModelUI {
        try {
            return db.collection(USER_PATH).document(email).get().await()
                .toObject(UserResponseModelData::class.java)?.toUi() ?: UserModelUI()
        } catch (e:Exception){
            Log.e("TAG ERICH", "getUser: ${e.message}", e)
            throw e
        }

    }/*
    ---------------------------- USER FINISHED ------------------------------------
     */


    /*
    ---------------------------- PROFILE ------------------------------------
     */
    suspend fun setProfile(email: String, profiles: ProfileResponseModelData): Boolean {
        val profile = hashMapOf(
            "id" to profiles.id,
            "name" to profiles.name,
            "categorys" to profiles.categorys,
        )
        try {
            db.collection(USER_PATH).document(email).collection(PROFILE_PATH)
                .document(profiles.id.orEmpty()).set(profile).await()
            return true
        } catch (e: Exception) {
            Log.e("TAG", "Dataservice setProfile: ${e.message}", e)
            throw e
        }
    }

    suspend fun getProfiles(email: String): List<ProfileModelUi> {
        try {
            return db.collection(USER_PATH).document(email).collection(PROFILE_PATH).get().await()
                .mapNotNull {
                    it.toObject(ProfileResponseModelData::class.java).toUi()
                }
        } catch (e:Exception){
            Log.e("TAG", "getProfiles: ${e.message}", e)
            throw e
        }
    }/*
    ---------------------------- PROFILE FINISHED ------------------------------------
     */


    /*
    ---------------------------- TRANSACTION ------------------------------------
     */
    suspend fun setTransaction(email:String, profileId:String, transaction: TransactionResponseModelData): Boolean {
        val tran = hashMapOf(
            "id" to transaction.id,
            "motive" to transaction.motive,
            "amount" to transaction.amount,
            "category" to transaction.category,
            "budget" to transaction.budget,
            "isBookmark" to transaction.isBookmark,
            "date" to transaction.date,
        )
        try {
            db.collection(USER_PATH).document(email).collection(PROFILE_PATH).document(profileId)
                .collection(TRANSACTION_PATH).document(transaction.id.orEmpty()).set(tran).await()
            return true
        } catch (e: Exception) {
            Log.e("TAG", "Dataservice setTransaction: ${e.message}", e)
            throw e
        }
    }

    fun getTransactions(email: String, profileId: String): Flow<List<TransactionModelUI>> {
        return db.collection(USER_PATH).document(email).collection(PROFILE_PATH).document(profileId)
            .collection(TRANSACTION_PATH).orderBy(TRANSACTION_DATE_PATH, Query.Direction.DESCENDING)
            .snapshots().map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.DTO()?.toUi()
                }
            }
    }

    fun removeTransaction(email: String, profileId: String, itemId: String) {
        db.collection(USER_PATH).document(email).collection(PROFILE_PATH).document(profileId)
            .collection(TRANSACTION_PATH).document(itemId).delete()
    }/*
    ---------------------------- TRANSACTION FINISHED ------------------------------------
     */

}