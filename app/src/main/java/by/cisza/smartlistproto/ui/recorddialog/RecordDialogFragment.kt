package by.cisza.smartlistproto.ui.recorddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.databinding.DialogRecordBinding
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.utils.errorText
import kotlinx.coroutines.launch

class RecordDialogFragment(private val listener: RecordDialogListener) : DialogFragment(), View.OnClickListener {

    private var _binding: DialogRecordBinding? = null
    private val binding get() = _binding

    private val viewModel: RecordDialogViewModel by viewModels()

    interface RecordDialogListener {
        fun onDialogResult(record: SmartRecord?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecordBinding.inflate(inflater)

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
            newRecordQuantity.editText?.setText(viewModel.viewState.value.quantity.toString())
            titleEditText.doOnTextChanged(viewModel::onTitleChanged)
            descriptionEditText.doOnTextChanged(viewModel::onDescriptionChanged)
            quantityEditText.doOnTextChanged(viewModel::onQuantityChanged)
            priceEditText.doOnTextChanged(viewModel::onPriceChanged)
            createButton.setOnClickListener(this@RecordDialogFragment::onClick)
            cancelButton.setOnClickListener(this@RecordDialogFragment::onClick)
        }
    }

    private fun updateView(state: RecordDialogViewState) {
        binding?.apply {
            newRecordTitle.isErrorEnabled = state.showError
            newRecordTitle.errorText(state.titleErrorRes)
        }

        if (state.createdRecord != null) {
            listener.onDialogResult(state.createdRecord)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.create_button -> viewModel.onCreateClick()
            R.id.cancel_button -> {
                listener.onDialogResult(null)
                dismiss()
            }
            R.id.quantity_edit_text -> {
                (v as EditText).let {
                    it.setSelection(0, it.text.length)
                }
            }
        }
    }
}