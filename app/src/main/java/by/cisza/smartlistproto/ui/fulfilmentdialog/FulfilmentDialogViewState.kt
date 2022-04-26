package by.cisza.smartlistproto.ui.fulfilmentdialog

import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
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