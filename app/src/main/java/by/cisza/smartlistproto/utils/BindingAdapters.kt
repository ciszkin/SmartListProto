package by.cisza.smartlistproto.utils

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.ui.recordlist.ReceiptItemsAdapter
import by.cisza.smartlistproto.ui.recordlist.SmartRecordAdapter

@BindingAdapter("textWatcher")
fun setTextWatcher(view: EditText, textWatcher: TextWatcher) {
    view.addTextChangedListener(textWatcher)
}

@BindingAdapter("receiptSource")
fun RecyclerView.updateReceipt(source: List<Receipt.ReceiptItem>) {
    this.apply {
        adapter = ReceiptItemsAdapter(
            source = source
        )
        layoutManager = LinearLayoutManager(this.context)
        hasFixedSize()
    }

}

@BindingAdapter(value = ["recordController", "recordSource"] )
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
