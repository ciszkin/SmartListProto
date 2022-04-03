package by.cisza.smartlistproto.ui.fulfilment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.databinding.DialogFulfilmentBinding
import by.cisza.smartlistproto.domain.Receipt.*
import by.cisza.smartlistproto.domain.SmartRecord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FulfilmentDialogFragment(
    private val listener: FulfilmentDialogListener,
    private val record: SmartRecord
) : DialogFragment() {

    private var _binding: DialogFulfilmentBinding? = null
    private val binding get() = _binding

    private val viewModel: FulfilmentDialogViewModel by lazy {
        ViewModelProvider(this).get(
            FulfilmentDialogViewModel::class.java
        )
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
        _binding = DialogFulfilmentBinding.inflate(inflater)
        viewModel.setCurrentRecord(record)

        binding?.apply {
            model = viewModel
            fulfilQuantity.setOnClickListener {
                fulfilQuantity.editText?.selectAll()
            }
            fulfilPrice.setOnClickListener {
                fulfilPrice.editText?.selectAll()
            }
            fulfilQuantity.editText?.setText(record.quantity.toString())
            fulfilPrice.editText?.setText(record.price.toString())

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect { state ->
                        binding?.state = state
                        state.receiptItem?.let {
                            listener.onDialogResult(it)
                            dismiss()
                        }
                    }
                }
            }
        }

        return binding!!.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}