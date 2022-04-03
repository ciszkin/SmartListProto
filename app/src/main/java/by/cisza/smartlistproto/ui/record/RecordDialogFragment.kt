package by.cisza.smartlistproto.ui.record

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.databinding.DialogRecordBinding
import by.cisza.smartlistproto.domain.SmartRecord
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ClassCastException

class RecordDialogFragment(private val listener: RecordDialogListener): DialogFragment() {

    private val binding: DialogRecordBinding by viewBinding(DialogRecordBinding::bind)
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
//        binding = DialogRecordBinding.inflate(inflater)

        binding?.apply {
            model = viewModel

//            onCreateClick = View.OnClickListener {
//                if (viewModel.validate()) {
//                    listener.onDialogResult(viewModel.createRecord())
//                    dismiss()
//                }
//            }
            newRecordQuantity.editText?.setText(viewModel.viewState.value.quantity.toString())
            newRecordQuantity.editText?.apply {
                setOnClickListener {
                    setSelection(0, this.text.length)
                }
            }

//            onCancelClick = View.OnClickListener {
//                listener.onDialogResult(null)
//                dismiss()
//            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect { state ->
                        binding?.state = state
//                        if (state.titleErrorRes != 0) {
//                            newRecordTitle.isErrorEnabled = true
//                            newRecordTitle.error = getString(state.titleErrorRes)
//                        } else {
//                            newRecordTitle.isErrorEnabled = false
//                        }
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
}