package by.cisza.smartlistproto.domain

import android.os.Parcelable
import by.cisza.smartlistproto.utils.round
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receipt(
    val id: Long,
    val date: String,
    val items: List<ReceiptItem>
): Parcelable {

    @Parcelize
    data class ReceiptItem(
        val recordId: Long,
        val title: String,
        val currency: String,
        val quantity: Double,
        val price: Double
    ): Parcelable {
        @IgnoredOnParcel
        val sum = (quantity * price).round(2)
    }
}