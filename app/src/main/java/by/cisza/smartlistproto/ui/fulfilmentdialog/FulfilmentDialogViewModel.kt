package by.cisza.smartlistproto.ui.fulfilmentdialog

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FulfilmentDialogViewModel @Inject constructor() : ViewModel() {

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
}