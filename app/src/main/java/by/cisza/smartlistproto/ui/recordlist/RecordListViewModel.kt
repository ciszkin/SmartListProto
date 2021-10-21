package by.cisza.smartlistproto.ui.recordlist

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.Receipt.ReceiptItem
import by.cisza.smartlistproto.domain.SmartRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class RecordListViewModel : ViewModel() {

    private val _records = MutableStateFlow(listOf<SmartRecord>())
    val records: StateFlow<List<SmartRecord>>
        get() = _records

    private var _receiptItems = MutableStateFlow(listOf<ReceiptItem>())
    val receiptItems: StateFlow<List<ReceiptItem>>
        get() = _receiptItems

    fun addRecord(record: SmartRecord) {

        val newList = mutableListOf<SmartRecord>()
        newList.addAll(records.value)
        newList.add(record)
        _records.value = newList
    }

    fun removeRecord(record: SmartRecord) {
        val newList = mutableListOf<SmartRecord>()
        newList.addAll(records.value)
        newList.remove(record)
        _records.value = newList
    }

    fun fulfilRecord(receiptItem: ReceiptItem) {

        val record = records.value.find {
            it.id == receiptItem.recordId
        }

        if (record != null) {
//            val position = records.value.indexOf(record)
//            val newRecord = record.copy(
//                completedQuantity = receiptItem.quantity
//            )
//            val newList = mutableListOf<SmartRecord>()
//            newList.addAll(records.value)
//            newList.remove(record)
//            newList.add(position, newRecord)
            _records.value = _records.value.map {
                if (it == record) it.copy(completedQuantity = receiptItem.quantity) else it
            }
        }

        val item = receiptItems.value.find {
            it.recordId == receiptItem.recordId && it.price == receiptItem.price
        }

        if (item != null) {
            _receiptItems.value = _receiptItems.value.map {
                if (it == item) it.copy(quantity = it.quantity + receiptItem.quantity) else it
            }
        } else {
            val newItems = mutableListOf<ReceiptItem>()
            newItems.addAll(receiptItems.value)
            newItems.add(receiptItem)
            _receiptItems.value = newItems
        }

    }

}