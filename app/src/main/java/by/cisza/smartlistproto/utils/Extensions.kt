package by.cisza.smartlistproto.utils

import java.util.*

fun Long.toDateString() : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return "$day.$month.$year"
}

fun Double.toAmount(unit: String) : String {
    return "$this $unit"
}