package com.dicoding.intermediate1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate1.databinding.ActivitySplashScreenBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val preferences = SettingPreferences.getInstance(dataStore)
        val viewModelSetting = ViewModelProvider(this, ViewModelSettingFactory(preferences))[ViewModelSetting::class.java]

        viewModelSetting.getProfilePreferences(Constanst.ProfilePreferences.ProfileToken.name)
            .observe(this) { token ->
                if (token == Constanst.defaultValue) Timer().schedule(4000) {
                    startActivity(Intent(this@SplashScreenActivity, AuthenticationActivity::class.java))
                    finish()
                } else Timer().schedule(4000) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
            }
    }
}