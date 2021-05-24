package by.cisza.smartlistproto.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmartRecord(
    val name: String,
    val date: Long,
    val description: String? = null,
    val sum: Double,
    val unit: String
): Parcelable