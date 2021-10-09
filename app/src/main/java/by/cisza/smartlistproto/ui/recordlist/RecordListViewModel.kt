package by.cisza.smartlistproto.ui.recordlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cisza.smartlistproto.domain.SmartRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RecordListViewModel : ViewModel(), SmartRecordAdapter.RecordController {

//    private var records = ArrayList<SmartRecord>()

    private val _records = MutableStateFlow(listOf<SmartRecord>())

    val records: StateFlow<List<SmartRecord>> get() = _records

    fun addRecord(record: SmartRecord) {

        val newList = mutableListOf<SmartRecord>()
        newList.addAll(records.value)
        newList.add(record)
        _records.value = newList

//        Log.e("MyDebug", "new list from records: ${records.value}")
//        records.add(record)
    }

    fun removeRecord(record: SmartRecord) {
        val newList = mutableListOf<SmartRecord>()
        newList.addAll(records.value)
        newList.remove(record)
        _records.value = newList
//        records.remove(record)
    }

//    fun getRecordsList() : List<SmartRecord> {
//        return records
//    }

    override fun setIsDone(record: SmartRecord) {
        if (records.value.contains(record)) {
            val position = records.value.indexOf(record)
            val newRecord = record.copy(isDone = true)
            val newList = mutableListOf<SmartRecord>()
            newList.addAll(records.value)
            newList.remove(record)
            newList.add(position, newRecord)
            _records.value = newList
        }
    }

}