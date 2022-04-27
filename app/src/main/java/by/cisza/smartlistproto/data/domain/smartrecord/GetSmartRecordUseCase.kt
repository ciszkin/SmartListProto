package by.cisza.smartlistproto.data.domain.smartrecord

import by.cisza.smartlistproto.data.entities.SmartRecord
import by.cisza.smartlistproto.data.repository.AppRepository
import by.cisza.smartlistproto.data.domain.UseCase
import javax.inject.Inject

class GetSmartRecordUseCase @Inject constructor(
    private val repository: AppRepository
) : UseCase<Long, SmartRecord>() {
    override suspend fun doWork(params: Long): SmartRecord {
        return repository.getSmartRecordById(params)
    }
}