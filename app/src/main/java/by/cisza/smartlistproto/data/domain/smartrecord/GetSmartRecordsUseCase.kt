package by.cisza.smartlistproto.data.domain.smartrecord

import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.data.repository.AppRepository
import by.cisza.smartlistproto.data.domain.UseCase
import javax.inject.Inject

class GetSmartRecordsUseCase @Inject constructor(
    private val repository: AppRepository
) : UseCase<Unit, List<SmartRecord>>() {
    override suspend fun doWork(params: Unit): List<SmartRecord> {
        return repository.getSmartRecords()
    }
}