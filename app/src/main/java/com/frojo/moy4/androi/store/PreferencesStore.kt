package com.frojo.moy4.androi.store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.frojo.moy4.androi.activity.CatLoadingActivity.Companion.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object PreferencesStore {
    private val urlKey = stringPreferencesKey("url_key")
    private const val EMPTY = "empty"

    suspend fun empty(store: DataStore<Preferences>): Boolean =
        readFromDataStore(store) == EMPTY

    suspend fun readFromDataStore(store: DataStore<Preferences>): String {
        return store.data.map { settings ->
            settings[urlKey] ?: EMPTY
        }.first()
    }

    suspend fun writeToDataStore(store: DataStore<Preferences>, url: String) {
        if (readFromDataStore(store) == EMPTY) {
            Log.d(TAG, "Save url: $url")
            store.edit { settings ->
                settings[urlKey] = url
            }
        }
    }
}