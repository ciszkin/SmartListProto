package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import kotlinx.coroutines.flow.Flow

class Repository(
    private val recordStore: SmartRecordStore,
    private val receiptStore: ReceiptStore,
    private val receiptItemStore: ReceiptItemStore
) : AppRepository {
    override fun getRecords(): Flow<List<SmartRecord>> {
        TODO("Not yet implemented")
    }

    override fun getReceiptItems(recordId: Long): Flow<List<ReceiptItem>> {
        TODO("Not yet implemented")
    }

    override fun getReceipts(): Flow<List<Receipt>> {
        TODO("Not yet implemented")
    }

}