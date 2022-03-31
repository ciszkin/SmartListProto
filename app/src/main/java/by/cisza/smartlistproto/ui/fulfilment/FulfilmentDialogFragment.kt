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
        viewModel.currentRecord = record

        with(binding) {
            model = viewModel
            fulfilQuantity.setOnClickListener {
                fulfilQuantity.editText?.selectAll()
            }
            fulfilPrice.setOnClickListener {
                fulfilPrice.editText?.selectAll()
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

}