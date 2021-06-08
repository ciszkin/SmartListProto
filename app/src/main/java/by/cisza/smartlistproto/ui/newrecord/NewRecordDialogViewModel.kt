package by.cisza.smartlistproto.ui.newrecord

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.domain.SmartRecord
import java.util.*

class NewRecordDialogViewModel: ViewModel() {

    var unit: String = "piece"
    var currency: String = "BYN"

    var title: String = ""
    var description: String = ""
    var quantity: Double = 1.0
    var price: Double = 0.0

    var titleErrorRes  = 0

    fun createRecord() : SmartRecord = SmartRecord(
        title = title,
        date = Calendar.getInstance().timeInMillis,
        description = description,
        quantity = quantity,
        price = price,
        sum = quantity*price,
        unit = unit,
        currency = currency
    )

    fun validate() : Boolean {
        titleErrorRes = if (title.isEmpty() || title.isBlank()) R.string.empty_tytle_error else 0

        return titleErrorRes == 0
    }
}