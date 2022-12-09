package com.dicoding.intermediate1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelSettingFactory(private val preferences: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSetting::class.java)) {
            return ViewModelSetting(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }
}
