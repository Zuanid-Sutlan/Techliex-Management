package com.techliexai.management.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.techliexai.management.data.utils.Constant
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class UserPreferencesRepositoryImpl(private val prefs: DataStore<Preferences>): UserPreferencesRepository {

    companion object {
        private val USERNAME_PREF_KEY = stringPreferencesKey(Constant.USERNAME)
        private val NAME_PREF_KEY = stringPreferencesKey(Constant.NAME)
        private val USER_ID_PREF_KEY = intPreferencesKey(Constant.USER_ID)
        private val USER_ROLE_PREF_KEY = stringPreferencesKey(Constant.USER_ROLE)
    }

    override suspend fun saveUser(user: User) {
        prefs.edit { preferences ->
            preferences[USERNAME_PREF_KEY] = user.username
            preferences[NAME_PREF_KEY] = user.name
            preferences[USER_ID_PREF_KEY] = user.id
            preferences[USER_ROLE_PREF_KEY] = user.role
        }
    }

    override fun getUser(): Flow<User> {
        return prefs.data.map { preferences ->
            val username = preferences[USERNAME_PREF_KEY] ?: ""
            val name = preferences[NAME_PREF_KEY] ?: ""
            val userId = preferences[USER_ID_PREF_KEY] ?: -1
            val userRole = preferences[USER_ROLE_PREF_KEY] ?: ""
            User(username = username, name = name, id = userId, role = userRole)
        }
    }

    override suspend fun clearUser() {
        prefs.edit { preferences ->
            preferences.clear()
        }
    }
}