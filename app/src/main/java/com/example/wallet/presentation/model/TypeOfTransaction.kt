package com.example.wallet.presentation.model

sealed class TypeOfTransaction (val state:Int){
    object EMPTY: TypeOfTransaction(0)
    object INCOME: TypeOfTransaction(1)
    object EXPENSES: TypeOfTransaction(2)


    companion object{
        fun getType(state: Int): TypeOfTransaction {
            return when{
                state == INCOME.state -> INCOME
                state == EXPENSES.state -> EXPENSES
                else -> EMPTY
            }
        }
    }
}