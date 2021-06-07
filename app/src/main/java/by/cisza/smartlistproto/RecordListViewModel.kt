package by.cisza.smartlistproto

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.domain.SmartRecord

class RecordListViewModel : ViewModel() {

    private var records = ArrayList<SmartRecord>()
    
    fun addRecord(record: SmartRecord) {
        records.add(record)
    }

    fun removeRecord(record: SmartRecord) {
        records.remove(record)
    }

    fun getRecordsList() : List<SmartRecord> {
        return records
    }

}