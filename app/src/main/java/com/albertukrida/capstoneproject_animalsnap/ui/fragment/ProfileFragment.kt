package com.albertukrida.capstoneproject_animalsnap.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentProfileBinding
import com.albertukrida.capstoneproject_animalsnap.helper.IntentHelper
import com.albertukrida.capstoneproject_animalsnap.helper.UserPreferences
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.fAuth
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import com.albertukrida.capstoneproject_animalsnap.ui.custom_view.MyButton
import com.albertukrida.capstoneproject_animalsnap.ui.custom_view.MyEditText
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mContext: Context
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mView = view

        // get user profile data
        ApiCall(mContext).refreshToken()
        Utils(mContext).updateProfPict(userModel.picture, binding)
        binding.tvUserId.text = buildString {
            append("User ID: ")
            append(userModel.userId)
        }
        binding.tvName.text = userModel.name
        binding.tvEmail.text = userModel.email
        binding.tvDate.text = userModel.date

        // change profile picture listener
        binding.ivCircleProfileImage.setOnClickListener{ changeProfilePictureDialog() }
        binding.ivSquareProfileImage.setOnClickListener{ changeProfilePictureDialog() }

        // change name listener
        binding.ivEditProfile.setOnClickListener{ changeNameDialog() }

        // change password listener
        binding.btnChangePass.setOnClickListener{ oldPasswordDialog() }

        // logout listener
        binding.btnLogout.setOnClickListener{ logout() }
    }

    private fun changeProfilePictureDialog() {
        val changeProfPictDialog = AlertDialog.Builder(mContext)
                .setView(R.layout.dialog_profile_picture)
                .setTitle("Change Profile Picture?")
                .create()
        changeProfPictDialog.show()
        assert(fAuth.currentUser != null)

        val profileIcon1 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon1)!!
        val profileIcon2 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon2)!!
        val profileIcon3 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon3)!!
        val profileIcon4 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon4)!!
        val profileIcon5 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon5)!!
        val profileIcon6 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon6)!!
        val profileIcon7 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon7)!!
        val profileIcon8 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon8)!!
        val profileIcon9 = changeProfPictDialog.findViewById<ImageView>(R.id.profileIcon9)!!
        val btnNext = changeProfPictDialog.findViewById<TextView>(R.id.btnNext)!!
        val btnCancel = changeProfPictDialog.findViewById<TextView>(R.id.btnCancel)!!

        val apiCall = ApiCall(mContext)
        profileIcon1.setOnClickListener {
            apiCall.updateAvatar("profile_icon1.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon2.setOnClickListener {
            apiCall.updateAvatar("profile_icon2.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon3.setOnClickListener {
            apiCall.updateAvatar("profile_icon3.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon4.setOnClickListener {
            apiCall.updateAvatar("profile_icon4.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon5.setOnClickListener{
            apiCall.updateAvatar("profile_icon5.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon6.setOnClickListener {
            apiCall.updateAvatar("profile_icon6.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon7.setOnClickListener {
            apiCall.updateAvatar("profile_icon7.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon8.setOnClickListener {
            apiCall.updateAvatar("profile_icon8.png", binding)
            changeProfPictDialog.dismiss()
        }
        profileIcon9.setOnClickListener {
            apiCall.updateAvatar("profile_icon9.png", binding)
            changeProfPictDialog.dismiss()
        }
        btnNext.setOnClickListener {
            startGallery()
            changeProfPictDialog.dismiss()
        }
        btnCancel.setOnClickListener { changeProfPictDialog.dismiss() }
    }

    private fun startGallery() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(openGalleryIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data!!.data
            getFile = Utils(mContext).uriToFile(imageUri!!)
            getFile = Utils(mContext).centerCropImage(getFile as File)
            val file = Utils(mContext).reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/*".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "profile_picture",
                file.name,
                requestImageFile
            )
            ApiCall(mContext).updateProfPict(imageMultipart, imageUri.toString())
        }
    }

    private fun oldPasswordDialog() {
        val oldPasswordDialog = AlertDialog.Builder(mContext)
                .setView(R.layout.dialog_password_old)
                .setTitle("${resources.getString(R.string.change_password)}?")
                .create()
        oldPasswordDialog.show()
        val oldPassword = oldPasswordDialog.findViewById<EditText>(R.id.ed_pass_old)!!
        val btnNext = oldPasswordDialog.findViewById<TextView>(R.id.btn_next)!!
        val btnCancel = oldPasswordDialog.findViewById<TextView>(R.id.btn_cancel)!!
        val cbPassword = oldPasswordDialog.findViewById<CheckBox>(R.id.cb_password)!!

        // Show Password
        cbPassword.setOnClickListener{
            Utils(mContext).showPassword(oldPassword, cbPassword)
        }
        btnNext.setOnClickListener {
            val txtOldPassword = oldPassword.text.toString()
            if (txtOldPassword.isEmpty()) {
                oldPassword.error = resources.getString(R.string.old_pass_error1)
                oldPassword.requestFocus()
            } else {
                if (userModel.password == txtOldPassword) {
                    oldPasswordDialog.dismiss()
                    newPasswordDialog(txtOldPassword)
                } else {
                    oldPassword.error = resources.getString(R.string.old_pass_error2)
                    oldPassword.requestFocus()
                }
            }
        }
        btnCancel.setOnClickListener { oldPasswordDialog.dismiss() }
    }

    private fun newPasswordDialog(txtOldPassword: String) {
        val newPasswordDialog = AlertDialog.Builder(mContext)
                .setView(R.layout.dialog_password_new)
                .setTitle("${resources.getString(R.string.change_password)}?")
                .create()
        newPasswordDialog.show()

        val newPassword:MyEditText = newPasswordDialog.findViewById(R.id.ed_pass_new)!!
        val confirmNewPassword:MyEditText = newPasswordDialog.findViewById(R.id.ed_re_pass_new_)!!
        val btnNext:MyButton = newPasswordDialog.findViewById(R.id.btnNext)!!
        val cbPassword:CheckBox = newPasswordDialog.findViewById(R.id.cb_password)!!
        MyButton(mContext).validateChangePass(newPassword.text, confirmNewPassword.text, btnNext)

        // Edit Text
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                MyButton(mContext).validateChangePass(newPassword.text, confirmNewPassword.text, btnNext)
            }
        }
        newPassword.addTextChangedListener(watcher)
        confirmNewPassword.addTextChangedListener(watcher)

        // Show Password
        cbPassword.setOnClickListener{
            Utils(mContext).showPassword(newPassword, cbPassword, confirmNewPassword)
        }

        btnNext.setOnClickListener {
            val txtNewPassword = newPassword.text.toString()
            val txtConfirmNewPassword = confirmNewPassword.text.toString()
            ApiCall(mContext).changePassword(txtOldPassword, txtNewPassword, txtConfirmNewPassword)
            newPasswordDialog.dismiss()
        }
    }

    private fun changeNameDialog(){
        val changeNameDialog: android.app.AlertDialog =
            android.app.AlertDialog.Builder(mView.context)
                .setView(R.layout.dialog_change_name)
                .setTitle("Change Display Name?")
                .create()
        changeNameDialog.show()

        assert(fAuth.currentUser != null)

        val changeName = changeNameDialog.findViewById<EditText>(R.id.ed_name)
        val btnNext = changeNameDialog.findViewById<TextView>(R.id.btn_next)
        val btnCancel = changeNameDialog.findViewById<TextView>(R.id.btn_cancel)
        changeName.setText(userModel.name)

        btnNext.setOnClickListener {
            val txtChangeName = changeName.text.toString()
            ApiCall(mContext).changeName(txtChangeName, binding)
            changeNameDialog.dismiss()
        }
        btnCancel.setOnClickListener { changeNameDialog.dismiss() }
    }

    private fun logout(){
        val confirmDialog = AlertDialog.Builder(mContext)
            .setTitle(resources.getString(R.string.logoutConfirm))
            .setMessage(resources.getString(R.string.logoutConfirmText))
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                // sign out from fireAuth
                fAuth.signOut()
                // sign out from api
                val userPreference = UserPreferences(requireContext())
                userModel.name = null
                userModel.token = null
                userModel.session = null
                userPreference.setUser(userModel)
                IntentHelper().goToLoginPage(requireActivity())
            }.setNegativeButton(resources.getString(R.string.no), null)
        confirmDialog.show()
    }

    companion object{
        var getFile: File? = null
    }
}