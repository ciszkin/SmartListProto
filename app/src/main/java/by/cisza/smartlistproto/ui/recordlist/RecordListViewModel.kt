package by.cisza.smartlistproto.ui.recordlist

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.model.ReceiptItem
import by.cisza.smartlistproto.model.SmartRecord
import by.cisza.smartlistproto.utils.round
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecordListViewModel : ViewModel(), SmartRecordAdapter.RecordController {

    private val _viewState = MutableStateFlow(RecordListViewState())
    val viewState: StateFlow<RecordListViewState>
        get() = _viewState

    fun addRecord(record: SmartRecord?) {
        if (record != null) {
            _viewState.value = _viewState.value.let {
                val newList = mutableListOf<SmartRecord>()
                newList.addAll(it.records)
                newList.add(record)
                it.copy(records = newList, showNewRecordDialog = false)
            }
        } else {
            _viewState.value = _viewState.value.copy(showNewRecordDialog = false)
        }
    }

    fun removeRecord(record: SmartRecord) {
        _viewState.value = _viewState.value.let {
            val newList = mutableListOf<SmartRecord>()
            newList.addAll(it.records)
            newList.remove(record)
            it.copy(records = newList)
        }
    }

    fun fulfilRecord(receiptItem: ReceiptItem) {

        val record = _viewState.value.records.find {
            it.id == receiptItem.recordId
        }

        val item = _viewState.value.receiptItems.find {
            it.recordId == receiptItem.recordId && it.price == receiptItem.price
        }

        val newRecords = if (record != null) {
            _viewState.value.records.map {
                if (it == record) {
                    val newCompletedQuantity = it.completedQuantity + receiptItem.quantity
                    when {
                        newCompletedQuantity <= it.quantity -> {
                            it.copy(completedQuantity = newCompletedQuantity)
                        }
                        newCompletedQuantity > it.quantity -> {
                            it.copy(
                                completedQuantity = newCompletedQuantity,
                                quantity = newCompletedQuantity
                            )
                        }
                        else -> {
                            it.copy()
                        }
                    }
                } else it
            }
        } else _viewState.value.records

        val newReceiptItems = viewState.value.receiptItems.map {
            if (it == item) it.copy(quantity = it.quantity + receiptItem.quantity) else it
        } as MutableList

        if (newReceiptItems.find {it.recordId == receiptItem.recordId && it.price == receiptItem.price} == null) newReceiptItems.add(receiptItem)

        _viewState.value = _viewState.value.copy(
            records = newRecords,
            receiptItems = newReceiptItems,
            totalSum = newReceiptItems.sumOf { it.sum }.round(2),
            itemToFulfil = null
        )

    }

    override fun restoreRecord(record: SmartRecord) {
        _viewState.value.apply {
            _viewState.value = this.copy(records = this.records.map {
                if (it == record) it.copy(completedQuantity = 0.0) else it
            })
        }
    }

    override fun fulfilRecord(item: SmartRecord) {
        _viewState.value = _viewState.value.copy(itemToFulfil = item)
    }

    override fun addRecord() {
        _viewState.value = _viewState.value.copy(showNewRecordDialog = true)
    }

}