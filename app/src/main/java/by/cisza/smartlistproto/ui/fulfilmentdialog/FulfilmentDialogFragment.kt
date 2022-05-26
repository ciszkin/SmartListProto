package by.cisza.smartlistproto.ui.fulfilmentdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.databinding.DialogFulfilmentBinding
import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FulfilmentDialogFragment(
    private val listener: FulfilmentDialogListener,
    private val record: SmartRecord
) : DialogFragment(), View.OnClickListener {

    private var _binding: DialogFulfilmentBinding? = null
    private val binding get() = _binding

    private val viewModel: FulfilmentDialogViewModel by viewModels()

    interface FulfilmentDialogListener {
        fun onDialogResult(receiptItem: ReceiptItem?)
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
        _binding = DialogFulfilmentBinding.inflate(inflater)
        viewModel.setCurrentRecord(record)

        setupView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateView(state)
                }
            }
        }

        return binding!!.root
    }

    private fun setupView() {
        binding?.apply {
            quantityEditText.doOnTextChanged(viewModel::onQuantityChanged)
            priceEditText.doOnTextChanged(viewModel::onPriceChanged)
            fulfilButton.setOnClickListener(this@FulfilmentDialogFragment::onClick)
            cancelButton.setOnClickListener(this@FulfilmentDialogFragment::onClick)

            fulfilQuantity.editText?.setText(record.quantityLeft.toString())
            fulfilPrice.editText?.setText(record.price.toString())
        }
    }

    private fun updateView(state: FulfilmentDialogViewState) {
        binding?.apply {
            fulfilLabel.text = state.currentRecord.title
            totalSum.text = state.totalSum.toString()

        }

        state.receiptItem?.let {
            listener.onDialogResult(it)
            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fulfil_button -> viewModel.fulfilRecord()
            R.id.cancel_button -> {
                listener.onDialogResult(null)
                dismiss()
            }
            R.id.quantity_edit_text, R.id.price_edit_text -> {
                (v as EditText).let {
                    it.setSelection(0, it.text.length)
                }
            }
        }
    }
}