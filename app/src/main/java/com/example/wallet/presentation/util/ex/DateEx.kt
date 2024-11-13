package com.example.wallet.presentation.util.ex

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun Date.previousMonht(): Date{
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, -1) // Suma o resta meses

    return calendar.time
}
fun Date.getMonthAndYear (): Date{
//    this ?: return Date()

    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.time
}
fun Date.nextMonht(): Date{
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, 1) // Suma o resta meses

    return calendar.time
}


fun Date.getMonthAndYearString ():String{
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(this).uppercase()
}