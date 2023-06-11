package com.albertukrida.capstoneproject_animalsnap.ui

import android.Manifest
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.addCallback
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
        navView = binding.navigationView

        if(fragment == "camera"){
            replaceFragment(CameraFragment())
            navView.menu.findItem(R.id.take_picture).isChecked = true
        }else{
            replaceFragment(HomeFragment())
        }

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.take_picture -> replaceFragment(CameraFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }

        onBackPressedDispatcher.addCallback(this) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(resources.getString(R.string.exitApp))
                .setMessage(resources.getString(R.string.confirmExit))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ -> finish() }
                .setNegativeButton(resources.getString(R.string.no), null)
            val dialog = builder.create()
            dialog.show()
            val imageView = dialog.findViewById<ImageView>(android.R.id.icon)
            imageView?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
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
        lateinit var fragment: String

        const val CAMERA_X_RESULT = 200
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}