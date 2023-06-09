package com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.*
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataResponse
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentCameraBinding
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentHomeBinding
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentProfileBinding
import com.albertukrida.capstoneproject_animalsnap.helper.*
import com.albertukrida.capstoneproject_animalsnap.helper.UserPreferences
import com.albertukrida.capstoneproject_animalsnap.ui.AnimalActivity
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.CLASS
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.CLASS_DESC
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.DESC
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.DONATION
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.HABITATS
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.NAME
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.PICT
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.PICT_USER
import com.albertukrida.capstoneproject_animalsnap.ui.ClassifyResultActivity.Companion.STATUS
import com.albertukrida.capstoneproject_animalsnap.ui.LoginActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import com.albertukrida.capstoneproject_animalsnap.ui.RegisterActivity
import com.albertukrida.capstoneproject_animalsnap.ui.adapter.AnimalAdapter
import com.albertukrida.capstoneproject_animalsnap.ui.adapter.HabitatAdapter
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.CameraFragment
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
                        activity.loginWithFirebase(activity, alertDialog, responseBody, password)
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
                refreshToken()
            }
        })
    }

    fun getAllHabitats(context: Context, binding: FragmentHomeBinding, recyclerView: RecyclerView, keyword: String? = null) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().getAllHabitats()
        client.enqueue(object : Callback<List<AllHabitatsResponseItem>> {
            override fun onResponse(
                call: Call<List<AllHabitatsResponseItem>>,
                response: Response<List<AllHabitatsResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        // Set up your adapter and data
                        if(keyword != null){
                            val tempList = ArrayList<AllHabitatsResponseItem>()
                            for(habitat in responseBody){
                                if(habitat.nama.contains(keyword, ignoreCase = true)){
                                    tempList.add(habitat)
                                }
                            }
                            if(tempList.isEmpty()){
                                binding.tvEmptyHabitat.visibility = View.VISIBLE
                                binding.rvListHabitat.visibility = View.GONE
                            }else{
                                binding.tvEmptyHabitat.visibility = View.GONE
                                binding.rvListHabitat.visibility = View.VISIBLE
                                val adapter = HabitatAdapter(tempList)
                                recyclerView.adapter = adapter
                            }
                        } else{
                            val adapter = HabitatAdapter(responseBody)
                            recyclerView.adapter = adapter
                        }
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
            override fun onFailure(call: Call<List<AllHabitatsResponseItem>>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getAllAnimal(activity: Activity, context: Context, binding: FragmentHomeBinding, recyclerView: RecyclerView, keyword: String? = null) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().getAllAnimal()
        client.enqueue(object : Callback<List<AllAnimalsResponseItem>> {
            override fun onResponse(
                call: Call<List<AllAnimalsResponseItem>>,
                response: Response<List<AllAnimalsResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        // Set up your adapter and data
                        if(keyword != null){
                            val tempList = ArrayList<AllAnimalsResponseItem>()
                            for(animal in responseBody){
                                if(animal.nama.contains(keyword, ignoreCase = true)){
                                    tempList.add(animal)
                                }
                            }
                            if(tempList.isEmpty()){
                                binding.tvEmptyAnimal.visibility = View.VISIBLE
                                binding.rvListAnimal.visibility = View.GONE
                            }else{
                                binding.tvEmptyAnimal.visibility = View.GONE
                                binding.rvListAnimal.visibility = View.VISIBLE
                                val adapter = AnimalAdapter(tempList, activity)
                                recyclerView.adapter = adapter
                            }
                        } else{
                            val adapter = AnimalAdapter(responseBody, activity)
                            recyclerView.adapter = adapter
                        }
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
            override fun onFailure(call: Call<List<AllAnimalsResponseItem>>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun postClassify(activity: Activity, imageMultipart: MultipartBody.Part) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().postClassify(userModel.userId!!, imageMultipart)
        client.enqueue(object : Callback<ClassifyResponse> {
            override fun onResponse(
                call: Call<ClassifyResponse>,
                response: Response<ClassifyResponse>
            ) {
                if (response.isSuccessful) {
                    alertDialog.dismiss()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getClassifyResult(activity, responseBody.id)
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
            override fun onFailure(call: Call<ClassifyResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getAnimalById(activity: Activity, id: String) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().getAnimalById(id)
        client.enqueue(object : Callback<AnimalDetailResponse> {
            override fun onResponse(
                call: Call<AnimalDetailResponse>,
                response: Response<AnimalDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        NAME = responseBody.namaHewan
                        CLASS = responseBody.namaClass
                        STATUS = responseBody.statusHewan
                        PICT = responseBody.gambarHewan
                        DESC = responseBody.deskripsiHewan
                        CLASS_DESC = responseBody.deskripsiClass
                        DONATION = responseBody.donasi
                        HABITATS = responseBody.habitat
                        val intent = Intent(activity, AnimalActivity::class.java)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
            override fun onFailure(call: Call<AnimalDetailResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getClassifyResult(activity: Activity, id: String) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService().getClassifyResult(id)
        client.enqueue(object : Callback<AnimalDetailResponse> {
            override fun onResponse(
                call: Call<AnimalDetailResponse>,
                response: Response<AnimalDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        NAME = responseBody.namaHewan
                        CLASS = responseBody.namaClass
                        STATUS = responseBody.statusHewan
                        PICT = responseBody.gambarHewan
                        PICT_USER = responseBody.gambarHewanUser!!
                        DESC = responseBody.deskripsiHewan
                        CLASS_DESC = responseBody.deskripsiClass
                        DONATION = responseBody.donasi
                        HABITATS = responseBody.habitat
                        val intent = Intent(activity, ClassifyResultActivity::class.java)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        activity.finish()
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
            override fun onFailure(call: Call<AnimalDetailResponse>, t: Throwable) {
                alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getHistory(fragment: CameraFragment, binding: FragmentCameraBinding){
        val client = ApiConfig.getApiService().getHist(userModel.userId!!, 1)
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.data.isEmpty()){
                            CameraFragment.alertDialog.dismiss()
                            binding.tvEmptyHistory.visibility = View.VISIBLE
                            binding.rvListHistory.visibility = View.GONE
                        }else{
                            binding.tvEmptyHistory.visibility = View.GONE
                            binding.rvListHistory.visibility = View.VISIBLE
                            fragment.getListHistory()
                        }
                    }
                } else {
                    CameraFragment.alertDialog.dismiss()
                    try {
                        val data = response.errorBody()?.string()
                        val jObjError = data?.let { JSONObject(it) }
                        Utils(context).errorDialog(jObjError?.getString("msg").toString())
                    } catch (e: Exception) {
                        Utils(context).errorDialog("Error! " + e.message)
                    }
                }
            }
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                CameraFragment.alertDialog.dismiss()
                Utils(context).errorDialog(t.message.toString())
            }
        })
    }

    fun getProfile(activity: LoginActivity, prevResponse: LoginResponse, password: String) {
        alertDialog = pdLoading.show(builder)
        val client = ApiConfig.getApiService(prevResponse.token).getProfile(prevResponse.uid)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        alertDialog.dismiss()
                        // save to preferences
                        val userPreference = UserPreferences(context)
                        val userModel = UserModel()
                        userModel.userId = prevResponse.uid
                        userModel.picture = responseBody.profilePicture
                        userModel.name = responseBody.nama
                        userModel.email = responseBody.email
                        userModel.password = password
                        userModel.date = Utils(context).getDate(responseBody.dateJoined)
                        userModel.token = prevResponse.token
                        userModel.refresh_token = prevResponse.refreshToken
                        userModel.session = "LoggedIn"
                        userPreference.setUser(userModel)

                        Utils(context).successDialog(context.resources.getString(R.string.loginSuccess))
                        IntentHelper().goToHomePage(activity, "home")
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

    fun updateProfPict(imageMultipart: MultipartBody.Part, imageUri: String) {
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
                        // save to preferences
                        val userPreference = UserPreferences(context)
                        userModel.picture = imageUri
                        userPreference.setUser(userModel)
                        // success dialog
                        Utils(context).successDialogProf(responseBody.message)
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

    fun updateAvatar(avatar: String, binding: FragmentProfileBinding) {
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
                        Utils(context).updateProfPict(avatar, binding)
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
                        // save to preferences
                        val userPreference = UserPreferences(context)
                        userModel.name = name
                        userPreference.setUser(userModel)
                        // show to user
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
                            // save to preferences
                            val userPreference = UserPreferences(context)
                            userModel.password = newPass
                            userPreference.setUser(userModel)
                            // success dialog
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