package by.cisza.smartlistproto.ui.fulfilment

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.utils.round

class FulfilmentDialogViewModel: ViewModel() {

    var quantity: Double = 0.0
    var price: Double = 0.0

    val totalSum = (quantity * price).round(2)

    var currentRecord: SmartRecord = SmartRecord(
        id = 0L,
        title = "",
        price = 0.0,
        currency = ""
    )
    set(value) {
        quantity = value.quantityLeft
        price = value.price
        field = value
    }

     fun onQuantityChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            quantity = s.toString().toDoubleOrNull() ?: 0.0
        }


    fun onPriceChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            price = s.toString().toDoubleOrNull() ?: 0.0
        }


    fun fulfilRecord() : ReceiptItem {
        return ReceiptItem(
            recordId = currentRecord.id,
            title = currentRecord.title,
            quantity = quantity,
            price = price,
            currency = currentRecord.currency
        )
    }

    fun returnRecord() : ReceiptItem {
        return ReceiptItem(
            recordId = currentRecord.id,
            title = currentRecord.title,
            quantity = 0.0,
            price = currentRecord.price,
            currency = currentRecord.currency
        )
    }

    fun validate() : Boolean {
        return currentRecord != null
    }
}