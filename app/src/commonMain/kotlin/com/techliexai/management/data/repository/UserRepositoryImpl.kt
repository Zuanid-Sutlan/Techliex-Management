package com.techliexai.management.data.repository

import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserRepository
import dev.gitlive.firebase.firestore.FirebaseFirestoreException

class UserRepositoryImpl: UserRepository {

    private val accounts = FirebaseDatabase.getAccountReference()

    // Create user in the Firebase Realtime Database
    override suspend fun createUser(user: User): Result<String, DataError> {
        return safeCall {
            val userRef = accounts.document(user.username)
            // Set user details in the "accounts" node
            userRef.set(user)
            "User created successfully"
        }
    }

    // Fetch user by username
    override suspend fun getUserByUsername(username: String): Result<User, DataError> {
        return safeCall {
            val userRef = accounts.document(username)
            val snapshot = userRef.get()
            if (!snapshot.exists) throw Exception("User not found")
            snapshot.data()
        }
    }

    // Update user details in the Firebase Realtime Database
    override suspend fun updateUser(user: User): Result<User, DataError> {
        return safeCall {
            val userRef = accounts.document(user.username)
            // Update user details
            userRef.set(user)
            user // Return the updated user object
        }
    }

    // Delete user from the Firebase Realtime Database
    override suspend fun deleteUser(user: User): Result<Unit, DataError> {
        return safeCall {
            val userRef = accounts.document(user.username)
            // Delete user from the database
            userRef.delete()
        }
    }


}