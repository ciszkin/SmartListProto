package by.cisza.smartlistproto.ui.record

import by.cisza.smartlistproto.domain.SmartRecord

data class RecordDialogViewState(
    val currency: String = "BYN",
    val title: String = "",
    val description: String = "",
    val quantity: Double = 1.0,
    val price: Double = 0.0,
    val titleErrorRes: Int  = 0,
    val createdRecord: SmartRecord? = null,
    val cancelDialog: Boolean = false
) {
    val showError = titleErrorRes != 0
}