package com.example.wallet.presentation.model

sealed class EventItem(val event:Int) {
    object EDIT:EventItem(0)
    object DELETE:EventItem(1)
    object BOOKMARK:EventItem(2)
    object ADD:EventItem(3)
}