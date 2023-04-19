package by.cisza.smartlistproto.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cisza.smartlistproto.data.domain.receiptitem.GetReceiptItemsUseCase
import by.cisza.smartlistproto.data.domain.smartrecord.GetSmartRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getReceiptItemsUseCase: GetReceiptItemsUseCase,
    private val getSmartRecordUseCase: GetSmartRecordUseCase
) : ViewModel() {


    private val _viewState = MutableStateFlow(StatisticsViewState())
    val viewState: StateFlow<StatisticsViewState>
        get() = _viewState

    fun getData(recordId: Long) {

        _viewState.update { it.copy(loading = true) }

        val getRecordJob = viewModelScope.launch {
            _viewState.update {
                it.copy(record = getSmartRecordUseCase.invoke(recordId))
            }
        }
        val getListJob = viewModelScope.launch {
            _viewState.update {
                it.copy(items = getReceiptItemsUseCase.invoke(recordId))
            }
        }

        getRecordJob.invokeOnCompletion {
            if (it == null && getListJob.isCompleted) _viewState.update { state ->
                state.copy(loading = false)
            }
        }


        getListJob.invokeOnCompletion {
            if (it == null && getRecordJob.isCompleted) _viewState.update { state ->
                state.copy(loading = false)
            }
        }
    }
}