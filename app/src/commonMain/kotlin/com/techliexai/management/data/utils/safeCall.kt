package com.techliexai.management.data.utils

import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.FirebaseException
// Note: Some exceptions might not be available in KMP directly, so we map them as needed

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
    } catch (e: Exception) {
        // Fallback generic catch for KMP
        e.printStackTrace()
        when {
            e.message?.contains("network", ignoreCase = true) == true -> return Result.Failure(DataError.NoInternet)
            e.message?.contains("timeout", ignoreCase = true) == true -> return Result.Failure(DataError.RequestTimeout)
            e.message?.contains("auth", ignoreCase = true) == true -> return Result.Failure(DataError.AuthenticationError)
            else -> return Result.Failure(DataError.Unknown(e.message))
        }
    }

    // Return success if no exceptions
    return Result.Success(response)
}