package by.cisza.smartlistproto.ui.fulfilment

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.utils.round
import java.util.*

class FulfilmentDialogViewModel: ViewModel() {

    var quantity: Double = 0.0
    var price: Double = 0.0

    val totalSum = (quantity * price).round(2)

    var currentRecord: SmartRecord? = null
    set(value) {
        quantity = value?.quantityLeft ?: 0.0
        price = value?.price ?: 0.0
        field = value
    }

    fun fulfilRecord() : ReceiptItem {
        return ReceiptItem(
            recordId = currentRecord!!.id,
            title = currentRecord!!.title,
            quantity = quantity,
            price = price,
            currency = currentRecord!!.currency
        )
    }

    fun returnRecord() : ReceiptItem {
        return ReceiptItem(
            recordId = currentRecord!!.id,
            title = currentRecord!!.title,
            quantity = 0.0,
            price = currentRecord!!.price,
            currency = currentRecord!!.currency
        )
    }

    fun validate() : Boolean {
        return currentRecord != null
    }
}