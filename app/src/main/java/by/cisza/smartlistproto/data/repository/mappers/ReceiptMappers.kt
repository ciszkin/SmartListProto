package by.cisza.smartlistproto.data.repository.mappers

import by.cisza.smartlistproto.data.db.entities.DbReceipt
import by.cisza.smartlistproto.data.entities.Receipt

object ReceiptMappers {

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
