package com.albertukrida.capstoneproject_animalsnap.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentCameraBinding
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.albertukrida.capstoneproject_animalsnap.ui.CameraActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.CAMERA_X_RESULT
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.REQUEST_CODE_PERMISSIONS
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.REQUIRED_PERMISSIONS
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var mContext: Context
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mView = view

        if (!allPermissionsGranted(mContext)) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.ivCamera.setOnClickListener{ startCameraX() }
        binding.ivGallery.setOnClickListener{ startGallery() }
    }

    private fun startCameraX() {
        if (allPermissionsGranted(mContext)){
            val intent = Intent(activity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }else {
            val builder = AlertDialog.Builder(mContext)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(mContext.resources.getString(R.string.permission))
                .setMessage(mContext.resources.getString(R.string.permissionText))
                .setCancelable(false)
                .setNegativeButton(mContext.resources.getString(R.string.close), null)
            val dialog = builder.create()
            dialog.show()
            val imageView = dialog.findViewById<ImageView>(android.R.id.icon)
            imageView?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            myFile?.let { _ ->
                getFile = myFile
            }
        }
    }

    private fun startGallery() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(openGalleryIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data!!.data
            val myFile = Utils(mContext).uriToFile(imageUri!!)
            getFile = myFile
            val file = Utils(mContext).reduceFileImage(ProfileFragment.getFile as File)
            val requestImageFile = file.asRequestBody("image/*".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "picture",
                file.name,
                requestImageFile
            )
            ApiCall(mContext).postClassify(imageMultipart)
        }
    }

    private fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object{
        var getFile: File? = null
    }
}