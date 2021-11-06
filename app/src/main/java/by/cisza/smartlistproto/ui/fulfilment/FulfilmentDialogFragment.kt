package by.cisza.smartlistproto.ui.fulfilment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.cisza.smartlistproto.databinding.DialogFulfilmentBinding
import by.cisza.smartlistproto.domain.Receipt
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import java.lang.ClassCastException

class FulfilmentDialogFragment(fragment: Fragment, private val record: SmartRecord): DialogFragment() {

    private lateinit var listener: FulfilmentDialogListener
    private lateinit var binding: DialogFulfilmentBinding
    private lateinit var viewModel: FulfilmentDialogViewModel

    init {
        try {
            listener = fragment as FulfilmentDialogListener
        } catch (e: ClassCastException) {
            Toast.makeText(context, "${context.toString()} must implement DialogListener!", Toast.LENGTH_SHORT).show()
        }
    }

    interface FulfilmentDialogListener {
        fun onDialogResult(receiptItem: ReceiptItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFulfilmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(FulfilmentDialogViewModel::class.java)

        with(binding) {
            viewModel.currentRecord = record
            item = record
            fulfilQuantity.editText?.setOnClickListener {
                fulfilQuantity.editText?.selectAll()
            }
            quantityTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.quantity = s.toString().toDoubleOrNull() ?: 0.0
                }
            }
            fulfilPrice.editText?.setOnClickListener {
                fulfilPrice.editText?.selectAll()
            }
            priceTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.price = s.toString().toDoubleOrNull() ?: 0.0
                }
            }
            onFulfilClick = View.OnClickListener {
                if (viewModel.validate()) {
                    listener.onDialogResult(viewModel.fulfilRecord())
                    dismiss()
                }
            }
            onCancelClick = View.OnClickListener {
                listener.onDialogResult(viewModel.returnRecord())
                dismiss()
            }
        }

        return binding.root
    }

//    fun setRecord(record: SmartRecord) {
//        viewModel.currentRecord = record
//        with(binding) {
//            item = record
//        }
//    }

    open class TextWatcherImpl : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}