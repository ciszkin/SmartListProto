package by.cisza.smartlistproto.utils

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("textWatcher")
fun setTextWatcher(view: EditText, textWatcher: TextWatcher) {
    view.addTextChangedListener(textWatcher)
}