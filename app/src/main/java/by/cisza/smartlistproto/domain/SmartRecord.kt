package by.cisza.smartlistproto.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmartRecord(
    val title: String,
    val date: Long,
    val description: String? = null,
    val quantity: Double,
    val price: Double,
    val sum: Double,
    val unit: String,
    val currency: String,
    val tags: List<String> = emptyList()
): Parcelable