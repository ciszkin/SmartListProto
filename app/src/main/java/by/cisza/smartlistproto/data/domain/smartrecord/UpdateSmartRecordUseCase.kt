package by.cisza.smartlistproto.data.domain.smartrecord

import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.data.repository.AppRepository
import by.cisza.smartlistproto.data.domain.UseCase
import javax.inject.Inject

class UpdateSmartRecordUseCase @Inject constructor(
    private val repository: AppRepository
) : UseCase<SmartRecord, Unit>() {
    override suspend fun doWork(params: SmartRecord) {
        return repository.updateSmartRecord(params)
    }
}