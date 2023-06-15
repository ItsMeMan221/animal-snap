package com.albertukrida.capstoneproject_animalsnap.helper

import androidx.appcompat.app.AlertDialog
import com.albertukrida.capstoneproject_animalsnap.R

class ProgressBarHelper {
    fun show(progressDialog: AlertDialog.Builder): AlertDialog {
        progressDialog.setCancelable(false)
            .setView(R.layout.dialog_progress_bar)
            .create()
        return progressDialog.show()
    }
}