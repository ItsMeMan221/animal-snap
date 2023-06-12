package com.albertukrida.capstoneproject_animalsnap.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentCameraBinding
import com.albertukrida.capstoneproject_animalsnap.helper.ProgressBarHelper
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.albertukrida.capstoneproject_animalsnap.ui.CameraActivity
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.CAMERA_X_RESULT
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.REQUEST_CODE_PERMISSIONS
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.REQUIRED_PERMISSIONS
import com.albertukrida.capstoneproject_animalsnap.ui.MainViewModel
import com.albertukrida.capstoneproject_animalsnap.ui.ViewModelFactory
import com.albertukrida.capstoneproject_animalsnap.ui.adapter.HistoryAdapter
import com.albertukrida.capstoneproject_animalsnap.ui.adapter.LoadingStateAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var pdLoading: ProgressBarHelper
    private lateinit var builder: AlertDialog.Builder
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mView = view

        ApiCall(mContext).refreshToken()

        pdLoading = ProgressBarHelper()
        builder = AlertDialog.Builder(mContext)
        alertDialog = pdLoading.show(builder)

        binding.ivAddPhoto.setOnClickListener{ showDialogPickPhoto(view) }
        binding.ivTrash.setOnClickListener{ showDialogDeletePhoto() }
        binding.buttonAdd.setOnClickListener{
            if (getFile == null || binding.tvAddPhotoHint.visibility == View.VISIBLE) {
                Toast.makeText(
                    context, resources.getString(R.string.error_select_image),
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val file = Utils(mContext).reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/*".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "picture",
                    file.name,
                    requestImageFile
                )
                ApiCall(mContext).postClassify(requireActivity(), imageMultipart)
            }
        }

        ApiCall(mContext).getHistory(this, binding)
    }

    fun getListHistory() {
        val adapter = HistoryAdapter(requireActivity())
        val recyclerView = binding.rvListHistory
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up your adapter and data
        recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
        mainViewModel.history.observe(requireActivity()) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showDialogPickPhoto(view: View){
        if (!allPermissionsGranted(requireContext())) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val pickPhotoDialog = AlertDialog.Builder(view.context)
            .setView(R.layout.dialog_option_take_pict)
            .setTitle(requireActivity().resources.getString(R.string.select_picture_from))
            .create()
        pickPhotoDialog.show()

        val camera = pickPhotoDialog.findViewById<ImageView>(R.id.iv_camera)
        val gallery = pickPhotoDialog.findViewById<ImageView>(R.id.iv_gallery)

        camera?.setOnClickListener{
            startCameraX()
            pickPhotoDialog.dismiss()
        }
        gallery?.setOnClickListener{
            startGallery()
            pickPhotoDialog.dismiss()
        }
    }

    private fun startCameraX() {
        if (allPermissionsGranted(requireContext())){
            val intent = Intent(activity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(requireActivity().resources.getString(R.string.permission))
                .setMessage(requireActivity().resources.getString(R.string.permissionText))
                .setCancelable(false)
                .setNegativeButton(requireActivity().resources.getString(R.string.close), null)
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

            myFile?.let { file ->
                getFile = myFile
                binding.ivAddPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.tvAddPhotoHint.visibility = View.GONE
                binding.ivTrash.visibility = View.VISIBLE
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
            binding.ivAddPhoto.setImageURI(imageUri)
            binding.tvAddPhotoHint.visibility = View.GONE
            binding.ivTrash.visibility = View.VISIBLE
        }
    }

    private fun showDialogDeletePhoto(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.delete_photo))
            .setMessage(resources.getString(R.string.delete_photo_text))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                binding.ivAddPhoto.setImageResource(R.drawable.border_circle_dashed)
                binding.tvAddPhotoHint.visibility = View.VISIBLE
                binding.ivTrash.visibility = View.GONE
            }
            .setNegativeButton(resources.getString(R.string.no), null)
        val dialog = builder.create()
        dialog.show()
    }

    companion object{
        var getFile: File? = null
        lateinit var alertDialog: AlertDialog
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        @SuppressLint("StaticFieldLeak")
        lateinit var mView: View
    }
}