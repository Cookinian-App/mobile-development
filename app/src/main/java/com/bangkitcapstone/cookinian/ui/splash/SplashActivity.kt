package com.bangkitcapstone.cookinian.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bangkitcapstone.cookinian.data.preference.UserPreference
import com.bangkitcapstone.cookinian.databinding.ActivitySplashBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.main.MainActivity
import com.bangkitcapstone.cookinian.ui.welcome.WelcomeActivity
import com.google.android.material.color.MaterialColors

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val splashViewModel by viewModels<SplashViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = MaterialColors.getColor(View(this), com.google.android.material.R.attr.colorTertiary)
        }
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeThemeMode()

        Handler(Looper.getMainLooper()).postDelayed({
            checkIsLogin()
        }, 600L)
    }

    private fun checkIsLogin() {
        splashViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun observeThemeMode() {
        splashViewModel.getThemeMode().observe(this) { themeMode ->
            when (themeMode) {
               UserPreference.SYSTEM_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
               UserPreference.LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
               UserPreference.DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}