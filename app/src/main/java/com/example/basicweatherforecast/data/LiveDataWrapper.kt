package com.example.basicweatherforecast.data

class LiveDataWrapper<T>(
    val responseStatus: ResponseStatus,
    val response: T? = null,
    val errorMessage: String? = null
) {

    enum class ResponseStatus {
        SUCCESS, LOADING, ERROR
    }

    companion object {
        fun <T> loading() = LiveDataWrapper<T>(ResponseStatus.LOADING)
        fun <T> success(data: T) = LiveDataWrapper<T>(ResponseStatus.SUCCESS, data)
        fun <T> error(errMsg: String? = null) =
            LiveDataWrapper<T>(ResponseStatus.ERROR, null, errMsg)
    }
}