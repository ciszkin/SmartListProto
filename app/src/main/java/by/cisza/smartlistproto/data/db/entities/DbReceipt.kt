package by.cisza.smartlistproto.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbReceipt(
    @PrimaryKey val id: Long,
    val date: String
)