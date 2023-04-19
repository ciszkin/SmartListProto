package by.cisza.smartlistproto.data.domain

sealed class UseCaseResult<T>(data: T? = null, message: String? = null) {

    class Success<T>(data: T): UseCaseResult<T>(data = data)
    class Fail<T>(message: String): UseCaseResult<T>(message = message)
}