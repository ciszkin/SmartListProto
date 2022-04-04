package by.cisza.smartlistproto.data.db.dao

import androidx.room.*
import by.cisza.smartlistproto.data.db.entities.DbSmartRecord

@Dao
interface SmartRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg records: DbSmartRecord)

    @Delete
    fun delete(record: DbSmartRecord)

    @Update
    fun update(vararg records: DbSmartRecord)

    @Query("SELECT * FROM dbsmartrecord")
    fun getAll(): List<DbSmartRecord>
}