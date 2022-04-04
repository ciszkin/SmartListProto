package by.cisza.smartlistproto.ui.fulfilment

import by.cisza.smartlistproto.model.ReceiptItem
import by.cisza.smartlistproto.model.SmartRecord
import by.cisza.smartlistproto.utils.round

data class FulfilmentDialogViewState(
    val quantity: Double = 1.0,
    val price: Double = 0.0,
    val currentRecord: SmartRecord = SmartRecord(
        id = 0L,
        title = "",
        price = 0.0,
        currency = ""
    ),
    val receiptItem: ReceiptItem? = null
) {
    val totalSum = (quantity * price).round(2)
}