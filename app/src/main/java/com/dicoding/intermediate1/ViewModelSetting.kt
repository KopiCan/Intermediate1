package com.dicoding.intermediate1

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewModelSetting(private val preferences: SettingPreferences): ViewModel() {

    fun getProfilePreferences(properties: String): LiveData<String> {
        return when(properties){
            Constanst.ProfilePreferences.ProfileID.name -> preferences.getProfileID().asLiveData()
            Constanst.ProfilePreferences.ProfileName.name -> preferences.getProfileName().asLiveData()
            Constanst.ProfilePreferences.ProfileToken.name -> preferences.getProfileToken().asLiveData()
            Constanst.ProfilePreferences.ProfleEmail.name -> preferences.getProfileEmail().asLiveData()
            else -> preferences.getProfileID().asLiveData()
        }
    }

    fun setProfilePreferences(profileToken: String, profileID: String, profileName: String, profileEmail: String){
        viewModelScope.launch { preferences.saveLoginSession(profileToken,profileID,profileName,profileEmail) }
    }

    fun clearProfilePreferences() {
        viewModelScope.launch { preferences.clearLoginSession() }
    }

}
