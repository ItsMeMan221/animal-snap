package com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.*
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataResponse
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentProfileBinding
import com.albertukrida.capstoneproject_animalsnap.helper.ProgressBarHelper
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.albertukrida.capstoneproject_animalsnap.ui.LoginActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import com.albertukrida.capstoneproject_animalsnap.ui.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.*

class ApiCall(private val context: Context) {

    private lateinit var alertDialog: AlertDialog
    private var pdLoading = ProgressBarHelper()
    private var builder = AlertDialog.Builder(context)

    fun postLoginUser(activity: LoginActivity, email: String, password: String) {
        alertDialog = pdLoading.show(builder)
        val dataLogin = DataClassLoginRegister(email, password)

        val client = ApiConfig.getApiService(null).postLoginUser(dataLogin)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        activity.loginWithFirebase(alertDialog, responseBody, password)
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("message").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun postRegisterUser(activity: RegisterActivity, name: String, email: String, password: String, repPass: String) {
        alertDialog = pdLoading.show(builder)
        val dataRegister = DataClassLoginRegister(email, password, repPass, name)

        val client = ApiConfig.getApiService(null).postRegisterUser(dataRegister)
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        activity.registerToFirebase(alertDialog, email, password)
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("message").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun refreshToken() {
        val client = ApiConfig.getApiService(userModel.refresh_token).refreshToken()
        client.enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: Response<RefreshTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userModel.token = responseBody.token
                    }
                } else {
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("msg").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getProfile(binding: FragmentProfileBinding) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().getProfile(userModel.userId!!)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val date = Utils(context).getDate(responseBody.dateJoined)
                        Utils(context).updateProfPict(responseBody.profilePicture, binding)
                        binding.tvUserId.text = buildString {
                            append("User ID: ")
                            append(userModel.userId)
                        }
                        binding.tvName.text = responseBody.nama
                        binding.tvEmail.text = responseBody.email
                        binding.tvDate.text = date
                        alertDialog.dismiss()
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("msg").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun updateProfPict(view: View, imageMultipart: MultipartBody.Part) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().updateProfPict(userModel.userId!!, imageMultipart)
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        // success dialog
                        Utils(context).successDialogProf(responseBody.message, view)
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Toast.makeText(context, data, Toast.LENGTH_LONG).show()
                        Utils(context).errorDialog(jObjError?.getString("message").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun updateAvatar(view: View, avatar: String) {
        alertDialog = pdLoading.show(builder)
        val dataAvatar = DataClassAvatar(avatar)

        val client = ApiConfig.getApiService().updateAvatar(userModel.userId!!, dataAvatar)
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        // success dialog
                        Utils(context).successDialogProf(responseBody.message, view)
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Toast.makeText(context, data, Toast.LENGTH_LONG).show()
                        Utils(context).errorDialog(jObjError?.getString("message").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun deleteProfPict() {
        val client = ApiConfig.getApiService().deleteProfPict(userModel.userId!!)
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse( call: Call<DataResponse>, response: Response<DataResponse>) { }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) { }
        })
    }

    fun changeName(name: String, binding: FragmentProfileBinding) {
        alertDialog = pdLoading.show(builder)
        val dataChangeName = DataClassChangeName(name)

        val client = ApiConfig.getApiService().changeName(userModel.userId!!, dataChangeName)
        client.enqueue(object : Callback<DataResponse> {
            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        binding.tvName.text = name
                        // success dialog
                        Utils(context).successDialog(responseBody.message)
                    }
                } else {
                    alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("message").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun changePassword(oldPass: String, newPass: String, rePass: String) {
        alertDialog = pdLoading.show(builder)
        val fAuth = FirebaseAuth.getInstance()
        fAuth.currentUser!!.updatePassword(newPass).addOnSuccessListener {
            userModel.password = newPass

            val dataChangePass = DataClassChangePassword(oldPass, newPass, rePass)
            val client = ApiConfig.getApiService().changePassword(userModel.userId!!, dataChangePass)
            client.enqueue(object : Callback<DataResponse> {
                override fun onResponse(
                    call: Call<DataResponse>,
                    response: Response<DataResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            alertDialog.dismiss()
                            Utils(context).successDialog(context.resources.getString(R.string.change_pass_success))
                        }
                    } else {
                        alertDialog.dismiss()
                        try {
                            val data = response.errorBody()?.string()
                            val jObjError = data?.let { JSONObject(it) }
                            Utils(context).errorDialog(jObjError?.getString("message").toString())
                        } catch (e: Exception) {
                            Utils(context).errorDialog("Error! " + e.message)
                        }
                    }
                }
                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    alertDialog.dismiss()
                    Utils(context).errorDialog(t.message.toString())
                }
            })
        }
        .addOnFailureListener { e: Exception ->
            alertDialog.dismiss()
            Utils(context).errorDialog("Error! " + e.message)
        }
    }
}