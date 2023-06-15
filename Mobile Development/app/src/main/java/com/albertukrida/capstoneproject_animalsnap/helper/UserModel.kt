package com.albertukrida.capstoneproject_animalsnap.helper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var userId: String? = null,
    var picture: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var date: String? = null,
    var token: String? = null,
    var refresh_token: String? = null,
    var session: String? = null
) : Parcelable