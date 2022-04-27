package by.cisza.smartlistproto.ui.statistics

import by.cisza.smartlistproto.data.entities.ReceiptItem
import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.utils.round

data class StatisticsViewState(
    val loading: Boolean = false,
    val record: SmartRecord? = null,
    val items: List<ReceiptItem> = emptyList()
)