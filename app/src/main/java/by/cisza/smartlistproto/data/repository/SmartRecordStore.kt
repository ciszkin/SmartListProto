package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.db.dao.SmartRecordDao
import by.cisza.smartlistproto.data.entities.SmartRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SmartRecordStore(
    private val recordDao: SmartRecordDao
) {

    fun getSmartRecords() : Flow<List<SmartRecord>> = flow {
        recordDao.getAll().map {
            Mappers.mapDbSmartRecordToSmartRecord(it)
        }
    }

    fun addSmartRecord(record: SmartRecord) {
        recordDao.insertAll(Mappers.mapSmartRecordToDbSmartRecord(record))
    }

}