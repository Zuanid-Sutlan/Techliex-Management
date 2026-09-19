package com.techliexai.management.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.techliexai.management.data.utils.Constant
import java.io.File

actual object DataStoreFactory {
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            File(System.getProperty("user.home"), Constant.DATA_STORE_FILE_NAME)
        }
    }
}
