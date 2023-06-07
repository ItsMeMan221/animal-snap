package com.albertukrida.capstoneproject_animalsnap.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentCameraBinding
import com.albertukrida.capstoneproject_animalsnap.helper.CameraGalleryHelper
import com.albertukrida.capstoneproject_animalsnap.helper.CameraGalleryHelper.Companion.REQUEST_CODE_PERMISSIONS
import com.albertukrida.capstoneproject_animalsnap.helper.CameraGalleryHelper.Companion.REQUIRED_PERMISSIONS

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

        if (!CameraGalleryHelper(this, mContext).allPermissionsGranted(requireContext())) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.ivCamera.setOnClickListener{ CameraGalleryHelper(this, mContext).startCameraX(requireActivity()) }
        binding.ivGallery.setOnClickListener{ CameraGalleryHelper(this, mContext).startGallery() }
    }
}