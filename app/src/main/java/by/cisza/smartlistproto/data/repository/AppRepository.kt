package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord

interface AppRepository {
    fun getSmartRecords() : List<SmartRecord>
    fun addSmartRecord(record: SmartRecord)
    fun updateSmartRecord(record: SmartRecord)
    fun getReceiptItems(recordId: Long) : List<ReceiptItem>
    fun getReceipts() : List<Receipt>
    fun addReceipt(receipt: Receipt)
    fun getSmartRecordById(recordId: Long): SmartRecord
}