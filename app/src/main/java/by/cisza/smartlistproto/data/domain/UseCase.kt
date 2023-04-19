package by.cisza.smartlistproto.data.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R> {

    suspend operator fun invoke(params: P): R {
        return withContext(Dispatchers.IO) { doWork(params) }
    }

    protected abstract suspend fun doWork(params: P): R
}