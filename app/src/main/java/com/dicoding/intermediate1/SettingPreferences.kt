package com.dicoding.intermediate1

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constanst.preferencesName)

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey(Constanst.ProfilePreferences.ProfileToken.name)
    private val userID = stringPreferencesKey(Constanst.ProfilePreferences.ProfileID.name)
    private val name = stringPreferencesKey(Constanst.ProfilePreferences.ProfileName.name)
    private val email = stringPreferencesKey(Constanst.ProfilePreferences.ProfleEmail.name)


    fun getProfileToken(): Flow<String> = dataStore.data.map { it[token] ?:Constanst.defaultValue }
    fun getProfileID(): Flow<String> = dataStore.data.map { it[userID] ?:Constanst.defaultValue }
    fun getProfileName(): Flow<String> = dataStore.data.map { it[name] ?:Constanst.defaultValue }
    fun getProfileEmail(): Flow<String> = dataStore.data.map { it[email] ?:Constanst.defaultValue }

    suspend fun saveLoginSession(profileToken: String, profileID: String, profileName: String, profileEmail: String){
        dataStore.edit { preferences ->
            preferences[token] = profileToken
            preferences[userID] = profileID
            preferences[name] = profileName
            preferences[email] = profileEmail
        }
    }

    suspend fun clearLoginSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this){
                val temporaryInstance = SettingPreferences(dataStore)
                INSTANCE = temporaryInstance
                temporaryInstance
            }
        }
    }

}
