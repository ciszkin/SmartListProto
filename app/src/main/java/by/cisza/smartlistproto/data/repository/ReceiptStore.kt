package by.cisza.smartlistproto.data.repository

import by.cisza.smartlistproto.data.db.dao.ReceiptDao
import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.repository.mappers.ReceiptMappers
import javax.inject.Inject

class ReceiptStore @Inject constructor(
    private val receiptDao: ReceiptDao
) {

    fun getReceipts() : List<Receipt> {
        return receiptDao.getAll().map {
            ReceiptMappers.mapDbSReceiptToReceipt(it)
        }
    }

    fun addReceipt(receipt: Receipt) {
        receiptDao.insertAll(ReceiptMappers.mapReceiptToDbReceipt(receipt))
    }

    fun updateReceipt(receipt: Receipt) {
        receiptDao.update(ReceiptMappers.mapReceiptToDbReceipt(receipt))
    }

}