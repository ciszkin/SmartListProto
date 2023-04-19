package by.cisza.smartlistproto.ui.recordlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cisza.smartlistproto.data.domain.smartrecord.AddSmartRecordUseCase
import by.cisza.smartlistproto.data.domain.smartrecord.GetSmartRecordsUseCase
import by.cisza.smartlistproto.data.domain.receipt.SaveReceiptUseCase
import by.cisza.smartlistproto.data.domain.smartrecord.UpdateSmartRecordUseCase
import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.utils.round
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val getSmartRecordsUseCase: GetSmartRecordsUseCase,
    private val addSmartRecordUseCase: AddSmartRecordUseCase,
    private val updateSmartRecordUseCase: UpdateSmartRecordUseCase,
    private val saveReceiptUseCase: SaveReceiptUseCase
) : ViewModel(), SmartRecordAdapter.RecordController {

    private val _viewState = MutableStateFlow(RecordListViewState())
    val viewState = _viewState.asStateFlow()

    private val _event = MutableSharedFlow<RecordListEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                it.copy(records = getSmartRecordsUseCase(Unit))
            }
        }
    }

    fun addRecord(record: SmartRecord?) {
        if (record != null) {
            _viewState.update {
                val newList = mutableListOf<SmartRecord>()
                newList.addAll(it.records)
                newList.add(record)
                it.copy(records = newList)
            }
            viewModelScope.launch {
                addSmartRecordUseCase(record)
            }
        }
    }

    fun removeRecord(record: SmartRecord) {
        _viewState.update {
            val newList = mutableListOf<SmartRecord>()
            newList.addAll(it.records)
            newList.remove(record)
            it.copy(records = newList)
        }
    }

    fun handleReceiptItem(receiptItem: ReceiptItem?) {

        if (receiptItem == null) {
            viewModelScope.launch {
                _event.emit(UpdateViewEvent(viewState.value))
            }
            return
        }

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

        if (newReceiptItems.find { it.recordId == receiptItem.recordId && it.price == receiptItem.price } == null) newReceiptItems.add(
            receiptItem
        )

        _viewState.update {
            it.copy(
                records = newRecords,
                receiptItems = newReceiptItems,
                totalSum = newReceiptItems.sumOf { it.sum }.round(2)
            )
        }

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
        _viewState.update { state ->
            state.copy(records = state.records.map {
                if (it == record) it.copy(completedQuantity = 0.0) else it
            })
        }
        updateDbRecord(record)
    }

    override fun fulfilRecord(item: SmartRecord) {
        viewModelScope.launch {
            _event.emit(FulfilEvent(item))
        }
    }

    override fun showStatistics(record: SmartRecord) {
        viewModelScope.launch {
            _event.emit(ShowStatisticsEvent(record))
        }
    }

    fun saveReceipt() {
        val receipt = Receipt(
            id = Calendar.getInstance().timeInMillis,
            date = Calendar.getInstance().time.toString(),
            items = _viewState.value.receiptItems
        )

        val savingJob =
        viewModelScope.launch {
            saveReceiptUseCase.invoke(receipt)
            _event.emit(ReceiptSavedEvent(
                message = "Receipt saved successfully!"
            ))

        }

        savingJob.invokeOnCompletion { cause ->
            if (cause == null) {
                _viewState.update {
                    it.copy(receiptItems = emptyList())
                }
            }
        }
    }
}