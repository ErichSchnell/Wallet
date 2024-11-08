package com.example.wallet.presentation.util

import com.google.firebase.Timestamp
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun Long?.millisToDate():String{
    this ?: return ""
    return try{
        val date = Date(this)
        val sdf = SimpleDateFormat("EEEE dd MMMM", Locale.getDefault())
        sdf.format(date)
    }catch (e: Exception){
        ""
    }
}
fun Long?.toTimeStamp():Timestamp{
    this ?: return Timestamp.now()
    return try{
            val seconds = this / 1000
            val nanoseconds = ((this % 1000) * 1000000).toInt()
            Timestamp(seconds, nanoseconds)
    }catch (e: Exception){
        Timestamp.now()
    }
}


fun Long.getMonthAndYearString ():String{
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(Date(this)).uppercase()
}
fun Long.previousMonht(): Long{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar.add(Calendar.MONTH, -1) // Suma o resta meses

    return calendar.time.time
}
fun Long.nextMonht(): Long{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar.add(Calendar.MONTH, 1) // Suma o resta meses

    return calendar.time.time
}