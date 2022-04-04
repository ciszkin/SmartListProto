package by.cisza.smartlistproto.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receipt(
    val id: Long,
    val date: String,
    val items: List<ReceiptItem>
): Parcelable {

    @IgnoredOnParcel
    val sum = items.sumOf {
        it.sum
    }

}