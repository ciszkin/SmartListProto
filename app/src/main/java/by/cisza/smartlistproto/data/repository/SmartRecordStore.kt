package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.db.dao.SmartRecordDao
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.data.repository.mappers.SmartRecordMappers
import javax.inject.Inject

class SmartRecordStore @Inject constructor(
    private val recordDao: SmartRecordDao
) {

    fun getSmartRecords() : List<SmartRecord> {
        return recordDao.getAll().map {
            SmartRecordMappers.mapDbSmartRecordToSmartRecord(it)
        }
    }

    fun addSmartRecord(record: SmartRecord) {
        recordDao.insertAll(SmartRecordMappers.mapSmartRecordToDbSmartRecord(record))
    }

    fun updateSmartRecord(record: SmartRecord) {
        recordDao.update(SmartRecordMappers.mapSmartRecordToDbSmartRecord(record))
    }

    fun getSmartRecordById(recordId: Long) : SmartRecord {
        return SmartRecordMappers.mapDbSmartRecordToSmartRecord(recordDao.get(recordId))
    }

}