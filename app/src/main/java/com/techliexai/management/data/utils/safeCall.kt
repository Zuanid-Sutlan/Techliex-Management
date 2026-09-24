package com.techliexai.management.data.utils

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

// Custom sealed class to handle different data errors
sealed class DataError {
    object NoInternet : DataError()
    object RequestTimeout : DataError()
    object ServerError : DataError()
    object AuthenticationError : DataError()
    data class Unknown(val message: String?) : DataError()
    object Serialization : DataError()
}

sealed class Result<out T, out E> {
    data class Success<out T>(val data: T) : Result<T, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
}

suspend inline fun <reified T> safeCall(
    execute: suspend () -> T
): Result<T, DataError> {
    val response = try {
        execute()
    } catch (e: UnknownHostException) {
        return Result.Failure(DataError.NoInternet)
    } catch (e: UnresolvedAddressException) {
        return Result.Failure(DataError.NoInternet)
    } catch (e: SocketTimeoutException) {
        return Result.Failure(DataError.RequestTimeout)
    } catch (e: HttpRequestTimeoutException) {
        return Result.Failure(DataError.RequestTimeout)
    } catch (e: ClientRequestException) {
        if (e.response.status == HttpStatusCode.Unauthorized || e.response.status == HttpStatusCode.Forbidden) {
            return Result.Failure(DataError.AuthenticationError)
        }
        return Result.Failure(DataError.Unknown(e.message))
    } catch (e: ServerResponseException) {
        return Result.Failure(DataError.ServerError)
    } catch (e: RedirectResponseException) {
        return Result.Failure(DataError.Unknown(e.message))
    } catch (e: SerializationException) {
        return Result.Failure(DataError.Serialization)
    } catch (e: IOException) {
        return Result.Failure(DataError.NoInternet)
    } catch (e: Exception) {
        e.printStackTrace()
        return Result.Failure(DataError.Unknown(e.message))
    }

    return Result.Success(response)
}
