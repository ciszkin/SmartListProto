package by.cisza.smartlistproto.ui.recordlist

import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord

data class RecordListViewState(
    val records: List<SmartRecord> = listOf(),
    val receiptItems: List<ReceiptItem> = listOf(),
    val totalSum: Double = 0.0,
) {
    val isReceiptVisible = receiptItems.isNotEmpty()
}