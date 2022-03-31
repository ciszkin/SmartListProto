package by.cisza.smartlistproto.ui.record

import androidx.lifecycle.ViewModel
import by.cisza.smartlistproto.R
import by.cisza.smartlistproto.domain.SmartRecord
import by.cisza.smartlistproto.utils.round
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class RecordDialogViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(RecordDialogViewState())
    val viewState: StateFlow<RecordDialogViewState>
        get() = _viewState

    fun createRecord(): SmartRecord = SmartRecord(
        id = Calendar.getInstance().timeInMillis,
        title = viewState.value.title,
        description = viewState.value.description,
        quantity = viewState.value.quantity,
        price = viewState.value.price,
        currency = viewState.value.currency
    )

    fun onTitleChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newValue = if (!s.isNullOrEmpty())
            _viewState.value.copy(title = s.toString(), titleErrorRes = 0)
        else
            _viewState.value.copy(title = s.toString())
        _viewState.value = newValue
    }

    fun onDescriptionChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _viewState.value = _viewState.value.copy(description = s.toString())
    }

    fun onQuantityChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _viewState.value = _viewState.value.copy(quantity = s.toString().toDoubleOrNull() ?: 0.0)
    }

    fun onPriceChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _viewState.value = _viewState.value.copy(price = s.toString().toDoubleOrNull() ?: 0.0)
    }

    fun validate() : Boolean {
        _viewState.value = _viewState.value.let {
            it.copy(titleErrorRes = if (it.title.isEmpty() || it.title.isBlank()) R.string.empty_tytle_error else 0)
        }

            return _viewState.value.titleErrorRes == 0
    }
}