package com.albertukrida.capstoneproject_animalsnap.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.ui.CameraActivity
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.CameraFragment
import java.io.File

class CameraGalleryHelper(fragment: CameraFragment, private val context: Context) {
    fun startCameraX(activity: Activity) {
        if (allPermissionsGranted(context)){
            val intent = Intent(activity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }else {
            val builder = AlertDialog.Builder(context)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(context.resources.getString(R.string.permission))
                .setMessage(context.resources.getString(R.string.permissionText))
                .setCancelable(false)
                .setNegativeButton(context.resources.getString(R.string.close), null)
            val dialog = builder.create()
            dialog.show()
            val imageView = dialog.findViewById<ImageView>(android.R.id.icon)
            imageView?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
        }
    }

    private val launcherIntentCameraX = fragment.registerForActivityResult(
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

    fun startGallery() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(openGalleryIntent)
    }

    private var resultLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data!!.data
            val myFile = Utils(context).uriToFile(imageUri!!)
            getFile = myFile
        }
    }

    fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var getFile: File? = null
    }
}