package com.dicoding.intermediate1

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate1.databinding.FragmentSettingsBinding


class SettingsFragment: Fragment()  {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val viewModelSetting = ViewModelProvider(this, ViewModelSettingFactory(preferences))[ViewModelSetting::class.java]
        viewModelSetting.getProfilePreferences(Constanst.ProfilePreferences.ProfileName.name)
            .observe(viewLifecycleOwner){
                binding.tvProfileName.text = it
            }
        viewModelSetting.getProfilePreferences(Constanst.ProfilePreferences.ProfleEmail.name)
            .observe(viewLifecycleOwner){
                binding.tvEmail.text = it
            }
        viewModelSetting.getProfilePreferences(Constanst.ProfilePreferences.ProfileToken.name)
            .observe(viewLifecycleOwner){
                if (it == Constanst.defaultValue){
                    (activity as MainActivity).movetoAuth()
                }
            }
        binding.tvLanguageCustomization.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.tvLogOut.setOnClickListener {
            viewModelSetting.clearProfilePreferences()
        }
    }

}
