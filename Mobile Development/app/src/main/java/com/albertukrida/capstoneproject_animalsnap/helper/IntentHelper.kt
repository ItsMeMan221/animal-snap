package com.albertukrida.capstoneproject_animalsnap.helper

import android.app.Activity
import android.content.Intent
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity
import com.albertukrida.capstoneproject_animalsnap.ui.LoginActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.fragment
import com.albertukrida.capstoneproject_animalsnap.ui.RegisterActivity
import android.R as R1

class IntentHelper {
    fun goToLoginPage(activity: Activity){
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R1.anim.fade_in, R1.anim.fade_out)
        activity.finish()
    }

    fun goToRegisterPage(activity: Activity){
        val intent = Intent(activity, RegisterActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R1.anim.fade_in, R1.anim.fade_out)
        activity.finish()
    }

    fun goToHomePage(activity: Activity, frag: String){
        fragment = frag
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R1.anim.fade_in, R1.anim.fade_out)
        activity.finish()
    }
}