package my.training.core.core_api.data.model

sealed interface NetworkResponse<out T> {
    data class Success<T>(val data: T) : NetworkResponse<T>
    data class Failure<T>(val data: T? = null, val error: Throwable?) : NetworkResponse<T>
}