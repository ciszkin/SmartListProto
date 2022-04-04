package by.cisza.smartlistproto.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbSmartRecord(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String?,
    val quantity: Double,
    val completedQuantity: Double,
    val price: Double,
    val currency: String,
    val tagsString: String?
)