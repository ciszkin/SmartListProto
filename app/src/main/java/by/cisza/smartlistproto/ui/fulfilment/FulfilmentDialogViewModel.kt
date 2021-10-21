package by.cisza.smartlistproto.ui.fulfilment

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.utils.round
import java.util.*

class FulfilmentDialogViewModel: ViewModel() {

    var currentRecord: SmartRecord? = null

    var quantity: Double = 1.0
    var price: Double = 0.0



    fun fulfilRecord() : ReceiptItem {
        return ReceiptItem(
            recordId = currentRecord!!.id,
            title = currentRecord!!.title,
            quantity = quantity,
            price = price,
            currency = currentRecord!!.currency
        )
    }

    fun validate() : Boolean {
        return currentRecord != null
    }
}