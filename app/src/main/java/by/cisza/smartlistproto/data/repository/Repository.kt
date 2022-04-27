package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import javax.inject.Inject

class Repository @Inject constructor(
    private val recordStore: SmartRecordStore,
    private val receiptStore: ReceiptStore,
    private val receiptItemStore: ReceiptItemStore
) : AppRepository {
    override fun getSmartRecords(): List<SmartRecord> {
        return recordStore.getSmartRecords()
    }

    override fun addSmartRecord(record: SmartRecord) {
        recordStore.addSmartRecord(record)
    }

    override fun updateSmartRecord(record: SmartRecord) {
        recordStore.updateSmartRecord(record)
    }

    override fun getReceiptItems(recordId: Long): List<ReceiptItem> {
        return receiptItemStore.getReceiptItemsBySmartRecordId(recordId)
    }

    override fun getReceipts(): List<Receipt> {
        return receiptStore.getReceipts().map {
            it.copy(
                items = receiptItemStore.getReceiptItemsByReceiptId(it.id)
            )
        }
    }

    override fun addReceipt(receipt: Receipt) {
        receiptItemStore.addReceiptItems(receipt.items, receipt.id)
        receiptStore.addReceipt(receipt)
    }

    override fun getSmartRecordById(recordId: Long): SmartRecord {
        return recordStore.getSmartRecordById(recordId)
    }

}