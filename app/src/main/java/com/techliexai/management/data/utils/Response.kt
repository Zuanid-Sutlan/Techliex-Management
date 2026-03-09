//package com.techliexai.management.data.utils
//
//sealed interface Response<out D, out E: DataError> {
//    data class Success<out D>(val data: D) : Response<D, Nothing>
//    data class Failure<out E: Error>(val error: E) : Response<Nothing, E>
//}
//
//inline fun <T, E: Error> Response<T, E>.onSuccess(action: (T) -> Unit): Response<T, E> {
//    if (this is Response.Success) action(data)
//    return this
//}
//
//inline fun <T, E: Error> Response<T, E>.onFailure(action: (E) -> Unit): Response<T, E> {
//    if (this is Response.Failure) action(error)
//    return this
//}
//
//sealed interface DataError: Error {
//    data object RequestTimeout : DataError
//    data object TooManyRequests : DataError
//    data object NoInternet : DataError
//    data object Server : DataError
//    data object Serialization : DataError
//    data class Unknown(val errorMessage: String? = null) : DataError
//}
//
//interface Error