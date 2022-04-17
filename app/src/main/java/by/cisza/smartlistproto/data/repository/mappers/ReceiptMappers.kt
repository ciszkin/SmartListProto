package by.cisza.smartlistproto.data.repository.mappers

import by.cisza.smartlistproto.data.db.entities.DbReceipt
import by.cisza.smartlistproto.data.db.entities.DbSmartRecord
import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.SmartRecord

object ReceiptMappers {

    const val LIST_SEPARATOR: String = "<*>"

    fun mapDbSReceiptToReceipt(from: DbReceipt) : Receipt {
        return Receipt(
            id = from.id,
            date = from.date,
            items = emptyList()
        )
    }

    fun mapReceiptToDbReceipt(from: Receipt): DbReceipt {
        return DbReceipt(
            id = from.id,
            date = from.date
        )
    }

}
