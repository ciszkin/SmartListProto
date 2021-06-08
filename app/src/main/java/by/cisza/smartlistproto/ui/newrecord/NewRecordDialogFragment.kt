package by.cisza.smartlistproto.ui.newrecord

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.cisza.smartlistproto.databinding.DialogAddRecordBinding
import by.cisza.smartlistproto.domain.SmartRecord
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.lang.ClassCastException

class NewRecordDialogFragment(fragment: Fragment): BottomSheetDialogFragment() {

    private lateinit var listener: NewRecordDialogListener
    private lateinit var binding: DialogAddRecordBinding
    private lateinit var viewModel: NewRecordDialogViewModel

    init {
        try {
            listener = fragment as NewRecordDialogListener
        } catch (e: ClassCastException) {
            Toast.makeText(context, "${context.toString()} must implement DialogListener!", Toast.LENGTH_SHORT).show()
        }
    }

    interface NewRecordDialogListener {
        fun onDialogResult(record: SmartRecord)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddRecordBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(NewRecordDialogViewModel::class.java)

        binding.apply {
            titleTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.title = s.toString()
                    if (!s.isNullOrEmpty()) {
                        viewModel.titleErrorRes = 0
                        titleError = false
                    }
                }
            }
            descriptionTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.description = s.toString()
                }
            }
            quantityTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.quantity = s.toString().toDoubleOrNull() ?: 0.0
                }
            }
            priceTextWatcher = object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.price = s.toString().toDoubleOrNull() ?: 0.0
                }
            }
            onCreateClick = View.OnClickListener {
                if (viewModel.validate()) {
                    listener.onDialogResult(viewModel.createRecord())
                    dismiss()
                } else {
                    titleError = true
                    newRecordTitle.error = getString(viewModel.titleErrorRes)
                }
            }
            newRecordQuantity.editText?.setText(viewModel.quantity.toString())

            onCancelClick = View.OnClickListener {
                dismiss()
            }
        }

        return binding.root
    }

    open class TextWatcherImpl : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}