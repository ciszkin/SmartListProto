package by.cisza.smartlistproto.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.databinding.DialogRecordBinding
import by.cisza.smartlistproto.domain.SmartRecord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecordDialogFragment(private val listener: RecordDialogListener): DialogFragment() {

    private var _binding: DialogRecordBinding? = null
    private val binding get() = _binding

    private val viewModel: RecordDialogViewModel by lazy { ViewModelProvider(this).get(RecordDialogViewModel::class.java) }

    interface RecordDialogListener {
        fun onDialogResult(record: SmartRecord?)
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
        _binding = DialogRecordBinding.inflate(inflater)

        binding?.apply {
            model = viewModel
            newRecordQuantity.editText?.setText(viewModel.viewState.value.quantity.toString())
            newRecordQuantity.editText?.apply {
                setOnClickListener {
                    setSelection(0, this.text.length)
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect { state ->
                        binding?.state = state

                        if (state.cancelDialog) {
                            listener.onDialogResult(null)
                            dismiss()
                        }
                        if (state.createdRecord != null) {
                            listener.onDialogResult(viewModel.createRecord())
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