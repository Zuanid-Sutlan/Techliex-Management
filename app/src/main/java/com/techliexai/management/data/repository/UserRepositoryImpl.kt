package com.techliexai.management.data.repository

import com.google.firebase.database.DatabaseException
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    private val accounts = Firebase.getAccountReference()

    // Create user in the Firebase Realtime Database
    override suspend fun createUser(user: User): Result<String, DataError> {
        return safeCall {
            val userRef = accounts.child(user.username)
            // Set user details in the "accounts" node
            userRef.setValue(user).await()
            "User created successfully"
        }
    }

    // Fetch user by username
    override suspend fun getUserByUsername(username: String): Result<User, DataError> {
        return safeCall {
            val userRef = accounts.child(username)
            val snapshot = userRef.get().await()
            snapshot.getValue(User::class.java) ?: throw DatabaseException("User not found")
        }
    }

    // Update user details in the Firebase Realtime Database
    override suspend fun updateUser(user: User): Result<User, DataError> {
        return safeCall {
            val userRef = accounts.child(user.username)
            // Update user details
            userRef.setValue(user).await()
            user // Return the updated user object
        }
    }

    // Delete user from the Firebase Realtime Database
    override suspend fun deleteUser(user: User): Result<Unit, DataError> {
        return safeCall {
            val userRef = accounts.child(user.username)
            // Delete user from the database
            userRef.removeValue().await()
        }
    }


}