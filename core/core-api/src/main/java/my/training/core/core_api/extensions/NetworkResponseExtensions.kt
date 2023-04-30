package my.training.core.core_api.extensions

import my.training.core.core_api.data.model.NetworkResponse

inline fun <Body> NetworkResponse<Body>.doOnSuccess(
    func: (Body) -> Unit
): NetworkResponse<Body> {
    if (this is NetworkResponse.Success) func(data)
    return this
}

inline fun <Body> NetworkResponse<Body>.doOnFailure(
    func: (NetworkResponse.Failure) -> Unit
): NetworkResponse<Body> {
    if (this is NetworkResponse.Failure) func(this)
    return this
}

fun NetworkResponse.Failure.getErrorMessage(): String {
    return when (this) {
        NetworkResponse.Failure.Connection -> "Проверьте подключение к сети"
        is NetworkResponse.Failure.Error -> throwable?.localizedMessage ?: "Неизвестная ошибка"
    }
}