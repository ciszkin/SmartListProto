package by.cisza.smartlistproto.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbReceiptItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recordId: Long,
    val receiptId: Long,
    val title: String,
    val currency: String,
    val quantity: Double,
    val price: Double
)