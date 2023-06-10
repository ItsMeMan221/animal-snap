package com.albertukrida.capstoneproject_animalsnap.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityCameraBinding
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityLoginBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}