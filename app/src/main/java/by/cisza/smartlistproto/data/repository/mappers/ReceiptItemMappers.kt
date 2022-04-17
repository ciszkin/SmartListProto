package by.cisza.smartlistproto.data.repository.mappers

import by.cisza.smartlistproto.data.db.entities.DbReceipt
import by.cisza.smartlistproto.data.db.entities.DbReceiptItem
import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem

object ReceiptItemMappers {

    const val LIST_SEPARATOR: String = "<*>"

    fun mapDbSReceiptItemToReceiptItem(from: DbReceiptItem) : ReceiptItem {
        return ReceiptItem(
            recordId = from.recordId,
            title = from.title,
            currency = from.currency,
            quantity = from.quantity,
            price = from.price
        )
    }

    fun mapReceiptItemToDbReceiptItem(from: ReceiptItem, receiptId: Long): DbReceiptItem {
        return DbReceiptItem(
            recordId = from.recordId,
            receiptId = receiptId,
            title = from.title,
            currency = from.currency,
            quantity = from.quantity,
            price = from.price
        )
    }

}
