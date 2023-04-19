package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.db.dao.ReceiptItemDao
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.repository.mappers.ReceiptItemMappers
import javax.inject.Inject

class ReceiptItemStore @Inject constructor(
    private val receiptItemDao: ReceiptItemDao
) {

    fun getReceiptItemsByReceiptId(receiptId: Long): List<ReceiptItem> {
        return receiptItemDao.getByReceiptId(receiptId).map {
            ReceiptItemMappers.mapDbSReceiptItemToReceiptItem(it)
        }
    }

    fun getReceiptItemsBySmartRecordId(recordId: Long): List<ReceiptItem> {
        return receiptItemDao.getBySmartRecordId(recordId).map {
            ReceiptItemMappers.mapDbSReceiptItemToReceiptItem(it)
        }
    }

    fun addReceiptItems(receiptItems: List<ReceiptItem>, receiptId: Long) {
        receiptItemDao.insertAll(
            receiptItems.map{
            ReceiptItemMappers.mapReceiptItemToDbReceiptItem(it, receiptId)
        })
    }
}