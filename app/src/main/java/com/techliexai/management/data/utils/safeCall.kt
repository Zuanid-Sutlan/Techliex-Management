package com.techliexai.management.data.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DatabaseException
import com.google.firebase.auth.FirebaseAuthException
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
        // Execute the function
        execute()
    } catch (e: UnknownHostException) {
        // Handle No Internet scenario
        return Result.Failure(DataError.NoInternet)
    } catch (e: UnresolvedAddressException) {
        // Handle No Internet scenario
        return Result.Failure(DataError.NoInternet)
    } catch (e: SocketTimeoutException) {
        // Handle Timeout scenario
        return Result.Failure(DataError.RequestTimeout)
    } catch (e: FirebaseNetworkException) {
        // Handle Firebase Network Error
        return Result.Failure(DataError.NoInternet)
    } catch (e: FirebaseAuthException) {
        // Handle Firebase Authentication Error
        return Result.Failure(DataError.AuthenticationError)
    } catch (e: DatabaseException) {
        // Handle Firebase Database error
        return Result.Failure(DataError.ServerError)
    } catch (e: Exception) {
        // Catch all other exceptions
        e.printStackTrace()
        return Result.Failure(DataError.Unknown(e.message))
    }

    // Return success if no exceptions
    return Result.Success(response)
}