package by.cisza.smartlistproto.ui.recordlist

import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.SmartRecord

data class RecordListViewState(
    val records: List<SmartRecord> = listOf(),
    val receiptItems: List<Receipt.ReceiptItem> = listOf(),
    val totalSum: Double = 0.0,
    val itemToFulfil: SmartRecord? = null,
    val showNewRecordDialog: Boolean = false
) {

    val isReceiptVisible = receiptItems.isNotEmpty()
}