package by.cisza.smartlistproto.ui.fulfilment

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.record.RecordDialogViewState
import by.cisza.smartlistproto.utils.round
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FulfilmentDialogViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(FulfilmentDialogViewState())
    val viewState: StateFlow<FulfilmentDialogViewState>
        get() = _viewState

    fun setCurrentRecord(record: SmartRecord) {
        _viewState.value = _viewState.value.copy(
            currentRecord = record,
            quantity = record.quantityLeft,
            price = record.price
        )
    }

    fun onQuantityChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _viewState.value = _viewState.value.copy(quantity = s.toString().toDoubleOrNull() ?: 0.0)
    }


    fun onPriceChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _viewState.value = _viewState.value.copy(price = s.toString().toDoubleOrNull() ?: 0.0)
    }


    fun fulfilRecord() {
        _viewState.value = _viewState.value.let {
            it.copy(
                receiptItem = ReceiptItem(
                    recordId = it.currentRecord.id,
                    title = it.currentRecord.title,
                    quantity = it.quantity,
                    price = it.price,
                    currency = it.currentRecord.currency
                )
            )
        }
    }

    fun returnRecord() {
        _viewState.value = _viewState.value.let {
            it.copy(
                receiptItem = ReceiptItem(
                    recordId = it.currentRecord.id,
                    title = it.currentRecord.title,
                    quantity = 0.0,
                    price = it.currentRecord.price,
                    currency = it.currentRecord.currency
                )
            )
        }
    }
}