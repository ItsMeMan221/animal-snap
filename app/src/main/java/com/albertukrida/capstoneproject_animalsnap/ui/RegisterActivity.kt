package com.albertukrida.capstoneproject_animalsnap.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityRegisterBinding
import com.albertukrida.capstoneproject_animalsnap.helper.IntentHelper
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.albertukrida.capstoneproject_animalsnap.ui.custom_view.MyButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()
        playAnimation()

        MyButton(this@RegisterActivity).validateRegister(binding)

        // Edit Text
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                MyButton(this@RegisterActivity).validateRegister(binding)
            }
        }
        binding.edRegisterName.addTextChangedListener(watcher)
        binding.edRegisterEmail.addTextChangedListener(watcher)
        binding.edRegisterPassword.addTextChangedListener(watcher)
        binding.edRegisterRepeatPassword.addTextChangedListener(watcher)

        // Show Password
        binding.cbPassword.setOnClickListener{
            Utils(this@RegisterActivity).
            showPassword(binding.edRegisterPassword, binding.cbPassword, binding.edRegisterRepeatPassword)
        }
        // Button On Click
        binding.btnLogin.setOnClickListener { IntentHelper().goToLoginPage(this@RegisterActivity) }
        binding.btnSubmitRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val repPass = binding.edRegisterRepeatPassword.text.toString()
            ApiCall(this@RegisterActivity).postRegisterUser(this@RegisterActivity, name, email, password, repPass)
        }
    }

    fun registerToFirebase(alertDialog: AlertDialog, email: String, password: String) {
        val fAuth = FirebaseAuth.getInstance()
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@RegisterActivity) { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                Objects.requireNonNull<FirebaseUser>(fAuth.currentUser).sendEmailVerification().addOnCompleteListener { task1: Task<Void?> ->
                    alertDialog.dismiss()
                    if (task1.isSuccessful) {
                        val verifyEmailDialog = AlertDialog.Builder(this@RegisterActivity)
                                .setView(R.layout.dialog_verif_email)
                                .setTitle(resources.getString(R.string.verify_email_title))
                                .create()
                        verifyEmailDialog.setCancelable(false)
                        verifyEmailDialog.show()

                        val btnNext = verifyEmailDialog.findViewById<TextView>(R.id.btnNext)
                        btnNext!!.setOnClickListener {
                            verifyEmailDialog.dismiss()
                            IntentHelper().goToLoginPage(this@RegisterActivity)
                        }
                    } else {
                        Utils(this@RegisterActivity).errorDialog(task1.exception!!.message.toString())
                    }
                }
            } else {
                alertDialog.dismiss()
                Utils(this@RegisterActivity).errorDialog(task.exception!!.message.toString())
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textView1, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.textView1, View.ALPHA, 1f).setDuration(300)
        val nameText = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val nameEdit = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(300)
        val emailEdit = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val passText = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(300)
        val passEdit = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val repPassText = ObjectAnimator.ofFloat(binding.textView5, View.ALPHA, 1f).setDuration(300)
        val repPassEdit = ObjectAnimator.ofFloat(binding.edRegisterRepeatPassword, View.ALPHA, 1f).setDuration(300)
        val cbPass = ObjectAnimator.ofFloat(binding.cbPassword, View.ALPHA, 1f).setDuration(300)
        val btnSubmit = ObjectAnimator.ofFloat(binding.btnSubmitRegister, View.ALPHA, 1f).setDuration(300)

        val name = AnimatorSet().apply {
            playTogether(nameText, nameEdit)
        }
        val email = AnimatorSet().apply {
            playTogether(emailText, emailEdit)
        }
        val password = AnimatorSet().apply {
            playTogether(passText, passEdit)
        }
        val repPassword = AnimatorSet().apply {
            playTogether(repPassText, repPassEdit)
        }

        AnimatorSet().apply {
            playSequentially(register, name, email, password, repPassword, cbPass, btnSubmit)
            start()
        }
    }
}