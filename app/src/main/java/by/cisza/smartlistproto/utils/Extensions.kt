package by.cisza.smartlistproto.utils

import java.util.*
import kotlin.math.roundToInt

fun Long.toDateString() : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return "$day.$month.$year"
}

fun Double.toAmount(currency: String) : String {
    return "$this $currency"
}

fun Double.round(precision: Int) : Double {
    return (this*(10.0.times(precision))).roundToInt()/(10.0.times(precision))
}