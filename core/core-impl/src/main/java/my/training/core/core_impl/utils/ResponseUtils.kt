package my.training.core.core_impl.utils

import my.training.core.core_api.data.model.NetworkResponse
import retrofit2.Response

internal suspend fun <T, R> safeNetworkCall(
    call: suspend () -> Response<T>,
    converter: (T?) -> R
): NetworkResponse<R> {
    return try {
        val response = call()
        if (response.isSuccessful && response.body() != null) {
            NetworkResponse.Success(
                data = converter(response.body()) ?: throw RuntimeException("Empty response body")
            )
        } else {
            NetworkResponse.Failure(
                data = converter(response.body()),
                error = Throwable(response.errorBody()?.string())
            )
        }
    } catch (e: Exception) {
        NetworkResponse.Failure(error = e)
    }
}