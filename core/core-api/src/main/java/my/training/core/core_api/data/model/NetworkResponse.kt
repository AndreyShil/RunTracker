package my.training.core.core_api.data.model

sealed interface NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>

    sealed interface Failure : NetworkResponse<Nothing> {
        data class Error(val throwable: Throwable?) : Failure
        object Connection : Failure
    }
}