package com.example.wallet.presentation.util.ex

/**
 * Returns this Double if it is positive or 0,
 * or the result of calling [defaultValue] function otherwise.
 */
fun Double.ifNotPositive(default: (Double) -> Double): Double = if (this >= 0) this else  default(this)
