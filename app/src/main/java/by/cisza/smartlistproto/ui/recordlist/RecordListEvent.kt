package by.cisza.smartlistproto.ui.recordlist

import by.cisza.smartlistproto.data.entities.SmartRecord

sealed class RecordListEvent

data class ReceiptSavedEvent(
    val message: String
) : RecordListEvent()

data class ShowStatisticsEvent(
    val record: SmartRecord
) : RecordListEvent()

data class FulfilEvent(
    val record: SmartRecord
) : RecordListEvent()

data class UpdateViewEvent(
    val state: RecordListViewState
) : RecordListEvent()