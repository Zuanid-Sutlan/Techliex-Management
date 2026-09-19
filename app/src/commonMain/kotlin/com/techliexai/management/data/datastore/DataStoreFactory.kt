package com.techliexai.management.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect object DataStoreFactory {
    fun create(): DataStore<Preferences>
}
