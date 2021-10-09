package by.cisza.smartlistproto.utils

import java.util.*
import kotlin.math.round

fun Long.toDateString() : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return "$day.$month.$year"
}

fun Double.toAmount(currency: String) : String {
    return "${this.round(2)} $currency"
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}