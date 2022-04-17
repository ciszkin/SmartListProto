package by.cisza.smartlistproto.data.db.dao

import androidx.room.*
import by.cisza.smartlistproto.data.db.entities.DbReceiptItem

@Dao
interface ReceiptItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg receiptItems: List<DbReceiptItem>)

    @Delete
    fun delete(receiptItem: DbReceiptItem)

    @Update
    fun update(vararg receiptItems: DbReceiptItem)

    @Query("SELECT * FROM dbreceiptitem")
    fun getAll() : List<DbReceiptItem>

    @Query("SELECT * FROM dbreceiptitem WHERE receiptId == :id")
    fun getByReceiptId(id: Long) : List<DbReceiptItem>

    @Query("SELECT * FROM dbreceiptitem WHERE recordId == :id")
    fun getBySmartRecordId(id: Long) : List<DbReceiptItem>
}