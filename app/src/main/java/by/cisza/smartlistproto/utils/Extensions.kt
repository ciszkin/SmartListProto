package by.cisza.smartlistproto.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.ui.recordlist.ReceiptItemsAdapter
import by.cisza.smartlistproto.ui.recordlist.SmartRecordAdapter
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.math.round

fun Long.toDateString() : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return "$day.$month.$year"
}

fun Double.toAmount(currency: String) : String {
    return "${this.round(2)} $currency"
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun RecyclerView.updateReceipt(source: List<ReceiptItem>) {
    this.apply {
        adapter = ReceiptItemsAdapter(
            source = source
        )
        layoutManager = LinearLayoutManager(this.context)
        hasFixedSize()
    }

}

fun RecyclerView.updateSmartList(recordController: SmartRecordAdapter.RecordController, source: List<SmartRecord>) {
    this.apply {
        adapter = SmartRecordAdapter(
            source = source,
            recordController = recordController
        )
        layoutManager = LinearLayoutManager(this.context)
        hasFixedSize()
    }

}

fun TextInputLayout.errorText(errorRes: Int) {
    if (errorRes != 0) error = this.context.getString(errorRes)
}