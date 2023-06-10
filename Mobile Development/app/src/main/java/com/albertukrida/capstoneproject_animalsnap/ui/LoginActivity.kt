package com.albertukrida.capstoneproject_animalsnap.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.LoginResponse
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityLoginBinding
import com.albertukrida.capstoneproject_animalsnap.helper.*
import com.albertukrida.capstoneproject_animalsnap.helper.UserPreferences
import com.albertukrida.capstoneproject_animalsnap.ui.custom_view.MyButton
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()
        playAnimation()

        MyButton(this@LoginActivity).validateLogin(binding)

        // Edit Text
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                MyButton(this@LoginActivity).validateLogin(binding)
            }
        }
        binding.edLoginEmail.addTextChangedListener(watcher)
        binding.edLoginPassword.addTextChangedListener(watcher)

        val utils = Utils(this@LoginActivity)
        // Show Password
        binding.cbPassword.setOnClickListener{
            utils.showPassword(binding.edLoginPassword, binding.cbPassword)
        }
        // resend verification
        binding.tvResendEmail.setOnClickListener {
            utils.resendEmail(binding)
        }
        // go to register
        binding.btnRegister.setOnClickListener {
            IntentHelper().goToRegisterPage(this@LoginActivity)
        }
        // submit login
        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            ApiCall(this@LoginActivity).postLoginUser(this@LoginActivity, email, password)
        }
    }

    fun loginWithFirebase(alertDialog: AlertDialog, responseBody: LoginResponse, password: String) {
        val fAuth = FirebaseAuth.getInstance()
        fAuth.signInWithEmailAndPassword(responseBody.email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                if (fAuth.currentUser!!.isEmailVerified) {
                    val userId = responseBody.uid
                    val email = responseBody.email
                    val token = responseBody.token
                    val refreshToken = responseBody.refreshToken
                    saveUser(userId, email, password, token, refreshToken)

                    alertDialog.dismiss()
                    Utils(this@LoginActivity).successDialog(resources.getString(R.string.loginSuccess))
                    IntentHelper().goToHomePage(this@LoginActivity)
                } else {
                    alertDialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({ binding.tvResendEmail.visibility = View.VISIBLE }, 7000)
                    Utils(this@LoginActivity).errorDialog(resources.getString(R.string.verify_email))
                }
            } else {
                alertDialog.dismiss()
                if(task.exception is FirebaseNetworkException){
                    Utils(this@LoginActivity).errorDialog(resources.getString(R.string.error_network))
                }else {
                    Utils(this@LoginActivity).errorDialog(task.exception!!.message.toString())
                }
            }
        }
    }

    private fun saveUser(userId: String, email: String, password: String, token: String, refreshToken: String) {
        val userPreference = UserPreferences(this@LoginActivity)
        val userModel = UserModel()
        userModel.userId = userId
        userModel.email = email
        userModel.password = password
        userModel.token = token
        userModel.refresh_token = refreshToken
        userModel.session = "LoggedIn"
        userPreference.setUser(userModel)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textView1, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.textView1, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val emailEdit = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)
        val passText = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(300)
        val passEdit = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(300)
        val cbPass = ObjectAnimator.ofFloat(binding.cbPassword, View.ALPHA, 1f).setDuration(300)
        val btnSubmit = ObjectAnimator.ofFloat(binding.btnSubmitLogin, View.ALPHA, 1f).setDuration(300)

        val email = AnimatorSet().apply {
            playTogether(emailText, emailEdit)
        }
        val password = AnimatorSet().apply {
            playTogether(passText, passEdit)
        }

        AnimatorSet().apply {
            playSequentially(login, email, password, cbPass, btnSubmit)
            start()
        }
    }
}