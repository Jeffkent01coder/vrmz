package com.jeff.vrmz.models

import android.graphics.Bitmap
import android.net.Uri

class UserRegData(
    val refId: Int = 1,
    val dob: String,
    val gender: String,
    val county: String,
    val subCounty: String,
    val ward: String,
    val location: String,
    val votingCenter: String
    )