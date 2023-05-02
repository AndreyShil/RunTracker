package my.training.core.core_impl.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import my.training.core.core_api.data.model.NetworkResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal suspend fun <T, R> safeNetworkCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> Response<T>,
    converter: (T?) -> R
): NetworkResponse<R> {
    return withContext(dispatcher) {
        try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                NetworkResponse.Success(
                    data = converter(response.body()) ?: throw RuntimeException("Empty response body")
                )
            } else {
                NetworkResponse.Failure.Error(
                    throwable = Throwable(response.errorBody()?.string())
                )
            }
        } catch (e: HttpException) {
            NetworkResponse.Failure.Connection
        } catch (e: IOException) {
            NetworkResponse.Failure.Connection
        } catch (e: Exception) {
            NetworkResponse.Failure.Error(e)
        }
    }
}