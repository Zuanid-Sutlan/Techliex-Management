package com.techliexai.management.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.techliexai.management.data.utils.Constant
import com.techliexai.management.presetation.utils.AndroidContextProvider

actual object DataStoreFactory {
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            AndroidContextProvider.context.preferencesDataStoreFile(name = Constant.DATA_STORE_FILE_NAME)
        }
    }
}
