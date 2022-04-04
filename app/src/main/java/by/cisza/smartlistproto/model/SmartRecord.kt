package by.cisza.smartlistproto.model

import android.os.Parcelable
import by.cisza.smartlistproto.utils.round
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmartRecord(
    val id: Long,
    val title: String,
    val description: String? = null,
    val quantity: Double = 0.0,
    val completedQuantity: Double = 0.0,
    val price: Double,
    val currency: String,
    val tags: List<String> = emptyList()
): Parcelable {

    @IgnoredOnParcel
    val sum = ((quantity - completedQuantity) * price).round(2)

    @IgnoredOnParcel
    val quantityLeft = quantity - completedQuantity
}