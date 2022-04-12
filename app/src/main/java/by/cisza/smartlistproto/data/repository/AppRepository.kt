package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getRecords() : Flow<List<SmartRecord>>
    fun getReceiptItems(recordId: Long) : Flow<List<ReceiptItem>>
    fun getReceipts() : Flow<List<Receipt>>
}