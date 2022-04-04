package by.cisza.smartlistproto.data.db.dao

import androidx.room.*
import by.cisza.smartlistproto.data.db.entities.DbReceipt

@Dao
interface ReceiptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg receipts: DbReceipt)

    @Delete
    fun delete(receipt: DbReceipt)

    @Update
    fun update(vararg receipt: DbReceipt)

    @Query("SELECT * FROM dbreceipt")
    fun getAll(): List<DbReceipt>
}