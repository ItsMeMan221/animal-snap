package com.albertukrida.capstoneproject_animalsnap.helper

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityLoginBinding
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentProfileBinding
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "dd-MMM-yyyy"

class Utils(private val context: Context) {

    private lateinit var pdLoading: ProgressBarHelper
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private val fAuth = FirebaseAuth.getInstance()
    private val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    //===========================================================================================//
    //========================================== LOGIN ==========================================//
    //===========================================================================================//

    fun showPassword(password: EditText, checkBox: CheckBox, repPassword: EditText ?= null){
        if (!checkBox.isChecked) {
            // hide password
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            repPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            // show password
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            repPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        if(password.text != null || "$password".isNotEmpty()){
            password.setSelection(password.text!!.length)
        }
        if(repPassword?.text != null || "$repPassword".isNotEmpty()){
            repPassword?.setSelection(repPassword.text!!.length)
        }
    }

    fun resendEmail(binding: ActivityLoginBinding) {
        pdLoading = ProgressBarHelper()
        builder = AlertDialog.Builder(context)
        alertDialog = pdLoading.show(builder)

        fAuth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                alertDialog.dismiss()
                binding.tvResendEmail.visibility = View.GONE
                Toast.makeText(context, context.resources.getString(R.string.verify_email_sent)
                        + " " + fAuth.currentUser!!.email + "!", Toast.LENGTH_LONG).show()
                binding.edLoginEmail.setText("")
                binding.edLoginPassword.setText("")
                binding.cbPassword.isChecked = false
                showPassword(binding.edLoginPassword, binding.cbPassword)
            } else {
                Toast.makeText(context, "Error! " + task.exception!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    //===========================================================================================//
    //========================================= PROFILE =========================================//
    //===========================================================================================//

    fun updateProfPict(url: String?, binding: FragmentProfileBinding) {
        val circleProfileImage = binding.ivCircleProfileImage
        val squareProfileImage = binding.ivSquareProfileImage
        // save to preferences
        val userPreference = UserPreferences(context)
        // show it to user
        when (url) {
            null -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon1)
                squareProfileImage.setImageResource(R.drawable.profile_icon1)
            }
            "profile_icon1.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon1)
                squareProfileImage.setImageResource(R.drawable.profile_icon1)
            }
            "profile_icon2.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon2)
                squareProfileImage.setImageResource(R.drawable.profile_icon2)
            }
            "profile_icon3.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon3)
                squareProfileImage.setImageResource(R.drawable.profile_icon3)
            }
            "profile_icon4.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon4)
                squareProfileImage.setImageResource(R.drawable.profile_icon4)
            }
            "profile_icon5.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon5)
                squareProfileImage.setImageResource(R.drawable.profile_icon5)
            }
            "profile_icon6.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon6)
                squareProfileImage.setImageResource(R.drawable.profile_icon6)
            }
            "profile_icon7.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon7)
                squareProfileImage.setImageResource(R.drawable.profile_icon7)
            }
            "profile_icon8.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon8)
                squareProfileImage.setImageResource(R.drawable.profile_icon8)
            }
            "profile_icon9.png" -> {
                userModel.picture = url
                userPreference.setUser(userModel)
                circleProfileImage.setImageResource(R.drawable.profile_icon9)
                squareProfileImage.setImageResource(R.drawable.profile_icon9)
            }
            else -> {
                userModel.picture = "custom"
                userPreference.setUser(userModel)
                Picasso.get().load(url).into(circleProfileImage)
                Picasso.get().load(url).into(squareProfileImage)
            }
        }
    }

    fun getDate(input: String): String {
        val chars = input.toCharArray()

        val date = "${chars[3]}${chars[4]}"                       // get date
        val month = getMonth("${chars[0]}${chars[1]}")      // get month
        val year = "${chars[6]}${chars[7]}${chars[8]}${chars[9]}"// get year

        return "$date $month $year"
    }

    fun getMonth(input: String): String {
        var output = ""
        when (input) {
            "01" -> output = context.resources.getString(R.string.january)
            "02" -> output = context.resources.getString(R.string.february)
            "03" -> output = context.resources.getString(R.string.march)
            "04" -> output = context.resources.getString(R.string.april)
            "05" -> output = context.resources.getString(R.string.may)
            "06" -> output = context.resources.getString(R.string.june)
            "07" -> output = context.resources.getString(R.string.july)
            "08" -> output = context.resources.getString(R.string.august)
            "09" -> output = context.resources.getString(R.string.september)
            "10" -> output = context.resources.getString(R.string.october)
            "11" -> output = context.resources.getString(R.string.november)
            "12" -> output = context.resources.getString(R.string.december)
        }
        return output
    }

    fun successDialogProf(text: String, view: View) {
        val successDialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_success)
            .create()
        successDialog.show()
        val txtSuccess: TextView = successDialog.findViewById(R.id.tv_success)!!
        txtSuccess.text = text
        successDialog.setOnDismissListener {
            // refresh profile fragment
            MainActivity.navView.selectedItemId = R.id.profile
            val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun successDialog(text: String){
        val successDialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_success)
            .create()
        successDialog.show()
        val txtSuccess: TextView = successDialog.findViewById(R.id.tv_success)!!
        txtSuccess.text = text
    }

    fun errorDialog(text: String){
        val errorDialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_error)
            .create()
        errorDialog.show()
        val txtError: TextView = errorDialog.findViewById(R.id.tv_error)!!
        txtError.text = text
    }

    //======================================= CAMERA AND GALLERY =======================================//
    //==================================================================================================//

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun getFileWidth(file: File): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)
        return options.outWidth
    }

    fun centerCropImage(file: File): File? {
        try {
            val width = getFileWidth(file)
            // Decode the input file into a Bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, options)

            // Calculate the scale factor to maintain the aspect ratio and center crop
            var scaleFactor = 1
            if (options.outWidth > width || options.outHeight > width) {
                val widthScale = options.outWidth.toFloat() / width.toFloat()
                val heightScale = options.outHeight.toFloat() / width.toFloat()
                scaleFactor = widthScale.coerceAtLeast(heightScale).toInt()
            }

            // Calculate the final dimensions for center cropping
            val scaledWidth = options.outWidth / scaleFactor
            val scaledHeight = options.outHeight / scaleFactor
            val left = (scaledWidth - width) / 2
            val top = (scaledHeight - width) / 2

            // Decode the input file into a scaled Bitmap with the desired dimensions
            options.inJustDecodeBounds = false
            options.inSampleSize = scaleFactor
            val bitmap = BitmapFactory.decodeFile(file.path, options)

            // Create a cropped bitmap using the calculated dimensions
            val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, width, width)

            // Create a new File to save the cropped image
            val croppedFile = File(file.parent, "cropped_image.jpg")

            // Compress the cropped bitmap to a file
            val outputStream = FileOutputStream(croppedFile)
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            // Recycle the bitmaps to release memory
            bitmap.recycle()
            croppedBitmap.recycle()

            return croppedFile
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}