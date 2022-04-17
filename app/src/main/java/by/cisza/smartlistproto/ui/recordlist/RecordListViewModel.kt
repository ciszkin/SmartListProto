package by.cisza.smartlistproto.ui.recordlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cisza.smartlistproto.data.domain.smartrecord.AddSmartRecordUseCase
import by.cisza.smartlistproto.data.domain.smartrecord.GetSmartRecordsUseCase
import by.cisza.smartlistproto.data.domain.smartrecord.UpdateSmartRecordUseCase
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.utils.round
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val getSmartRecordsUseCase: GetSmartRecordsUseCase,
    private val addSmartRecordUseCase: AddSmartRecordUseCase,
    private val updateSmartRecordUseCase: UpdateSmartRecordUseCase
) : ViewModel(), SmartRecordAdapter.RecordController {

    private val _viewState = MutableStateFlow(RecordListViewState())
    val viewState: StateFlow<RecordListViewState>
        get() = _viewState

    init {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(records = getSmartRecordsUseCase(Unit))
        }
    }

    fun addRecord(record: SmartRecord?) {
        if (record != null) {
            _viewState.value = _viewState.value.let {
                val newList = mutableListOf<SmartRecord>()
                newList.addAll(it.records)
                newList.add(record)
                it.copy(records = newList, showNewRecordDialog = false)
            }
            viewModelScope.launch {
                addSmartRecordUseCase(record)
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

    fun handleReceiptItem(receiptItem: ReceiptItem) {

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

        if (record != null) updateDbRecord(record)

    }

    private fun updateDbRecord(record: SmartRecord) {
        viewModelScope.launch {
            _viewState.value.records.find {
                it.id == record.id
            }?.let {
                updateSmartRecordUseCase(it)
            }
        }
    }

    override fun restoreRecord(record: SmartRecord) {
        _viewState.value.apply {
            _viewState.value = this.copy(records = this.records.map {
                if (it == record) it.copy(completedQuantity = 0.0) else it
            })
        }
        updateDbRecord(record)
    }

    override fun fulfilRecord(item: SmartRecord) {
        _viewState.value = _viewState.value.copy(itemToFulfil = item)
    }

    override fun addRecord() {
        _viewState.value = _viewState.value.copy(showNewRecordDialog = true)
    }

}