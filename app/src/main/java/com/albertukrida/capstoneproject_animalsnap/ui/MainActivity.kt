package com.albertukrida.capstoneproject_animalsnap.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityMainBinding
import com.albertukrida.capstoneproject_animalsnap.helper.UserModel
import com.albertukrida.capstoneproject_animalsnap.helper.UserPreferences
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.CameraFragment
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.HomeFragment
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        mUserPreference = UserPreferences(this)
        userModel = mUserPreference.getUser()
        fAuth = FirebaseAuth.getInstance()

        replaceFragment(HomeFragment())

        navView = binding.navigationView
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.take_picture -> replaceFragment(CameraFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransactions = fragmentManager.beginTransaction()
        fragmentTransactions.replace(R.id.frameLayout, fragment)
        fragmentTransactions.commit()
    }

    companion object{
        lateinit var userModel: UserModel
        lateinit var fAuth: FirebaseAuth
        lateinit var navView: BottomNavigationView

        const val CAMERA_X_RESULT = 200
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}