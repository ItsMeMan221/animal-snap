package com.albertukrida.capstoneproject_animalsnap.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.util.Patterns
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityLoginBinding
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class MyButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColorBlack: Int = 0
    private var txtColorWhite: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColorBlack)
        if(id == R.id.btnNext){
            if(isEnabled) {
                setTextColor(txtColorWhite)
            }else{
                setTextColor(txtColorBlack)
            }
        }
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColorBlack = ContextCompat.getColor(context, R.color.black)
        txtColorWhite = ContextCompat.getColor(context, R.color.white)
        if(id == R.id.btnNext){
            enabledBackground = ContextCompat.getDrawable(context, R.drawable.shape_button_green) as Drawable
            disabledBackground = ContextCompat.getDrawable(context, R.drawable.shape_button_disable) as Drawable
        }else {
            enabledBackground = ContextCompat.getDrawable(context, R.drawable.shape_button_white) as Drawable
            disabledBackground = ContextCompat.getDrawable(context, R.drawable.shape_button_disable) as Drawable
        }
    }

    fun validateLogin(binding: ActivityLoginBinding) {
        val email = binding.edLoginEmail.text
        val password = binding.edLoginPassword.text
        val login = binding.btnSubmitLogin

        // enable button submit
        if (email != null && "$email".isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password != null && "$password".isNotEmpty() && "$password".length >= 8){
            login.isEnabled = true
            login.text = resources.getString(R.string.login)
        } else{
            login.isEnabled = false
            login.text = resources.getString(R.string.noData)
        }
    }

    fun validateRegister(binding: ActivityRegisterBinding){
        val name = binding.edRegisterName.text
        val email = binding.edRegisterEmail.text
        val password = binding.edRegisterPassword.text
        val repPassword = binding.edRegisterRepeatPassword.text
        val register = binding.btnSubmitRegister

        // enable button submit
        if (name != null && "$name".isNotEmpty() && name.length > 3 && Pattern.compile("^[a-zA-Z ]+$").matcher(name).matches() &&
            email != null && "$email".isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password != null && "$password".isNotEmpty() && "$password".length >= 8
        ){
            if("$password" == "$repPassword") {
                register.isEnabled = true
                register.text = resources.getString(R.string.register)
            } else{
                register.isEnabled = false
                register.text = resources.getString(R.string.rep_pass_error)
            }
        } else{
            register.isEnabled = false
            register.text = resources.getString(R.string.noData)
        }
    }

    fun validateForgetPass(email: TextView, btnNext: TextView){
        // enable button submit
        if ("$email".isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            btnNext.isEnabled = true
            btnNext.text = resources.getString(R.string.ok)
        } else{
            btnNext.isEnabled = false
            btnNext.text = resources.getString(R.string.noData)
        }
    }

    fun validateChangePass(newPass: Editable?, repNewPass: Editable?, btnNext: MyButton){
        // enable button submit
        if ("$newPass".isNotEmpty() && "$newPass".length >= 8 && "$newPass" == "$repNewPass"){
            btnNext.isEnabled = true
            btnNext.text = resources.getString(R.string.ok)
        } else{
            btnNext.isEnabled = false
            btnNext.text = resources.getString(R.string.noData)
        }
    }
}