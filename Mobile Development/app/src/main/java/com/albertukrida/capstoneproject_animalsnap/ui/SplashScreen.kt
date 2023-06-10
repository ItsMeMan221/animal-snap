package com.albertukrida.capstoneproject_animalsnap.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivitySplashScreenBinding
import com.albertukrida.capstoneproject_animalsnap.helper.IntentHelper
import com.albertukrida.capstoneproject_animalsnap.helper.UserModel
import com.albertukrida.capstoneproject_animalsnap.helper.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var mUserPreference: UserPreferences
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mUserPreference = UserPreferences(this)
        userModel = mUserPreference.getUser()

        onBackPressedDispatcher.addCallback(this) { }

        Handler(Looper.getMainLooper()).postDelayed({
            if (userModel.token != null && userModel.token!!.isNotEmpty() &&
                userModel.session != null && userModel.session!!.isNotEmpty()) {
                IntentHelper().goToHomePage(this@SplashScreen)
            }else{
                IntentHelper().goToLoginPage(this)
            }
        }, 1500)
    }
}