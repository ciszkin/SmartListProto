package by.cisza.smartlistproto.data.domain.receipt

import by.cisza.smartlistproto.data.repository.AppRepository
import by.cisza.smartlistproto.data.domain.UseCase
import by.cisza.smartlistproto.data.entities.Receipt
import javax.inject.Inject

class SaveReceiptUseCase @Inject constructor(
    private val repository: AppRepository
) : UseCase<Receipt, Unit>() {
    override suspend fun doWork(params: Receipt) {
        repository.addReceipt(params)
    }
}