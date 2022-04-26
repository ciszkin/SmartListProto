package by.cisza.smartlistproto.data.domain.receiptitem

import by.cisza.smartlistproto.data.repository.AppRepository
import by.cisza.smartlistproto.data.domain.UseCase
import by.cisza.smartlistproto.data.entities.Receipt
import by.cisza.smartlistproto.data.entities.ReceiptItem
import javax.inject.Inject

class GetReceiptItemsUseCase @Inject constructor(
    private val repository: AppRepository
) : UseCase<Long, List<ReceiptItem>>() {
    override suspend fun doWork(params: Long) : List<ReceiptItem> {
        return repository.getReceiptItems(params)
    }
}