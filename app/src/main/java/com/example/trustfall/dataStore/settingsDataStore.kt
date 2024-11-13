package com.example.trustfall.dataStore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map



class settingsDataStore(var context: Context) {


    private companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "check_box")
        val LOGIN_CHECK_BOX_STATE = booleanPreferencesKey("check_box")
    }

    suspend fun saveState(state: Boolean){
        context.dataStore.edit {
            it[LOGIN_CHECK_BOX_STATE] = state
        }
    }
    suspend fun getFromDataStore(): Boolean {
        val p = context.dataStore.data.first()
        return p[LOGIN_CHECK_BOX_STATE] ?: false
    }
}