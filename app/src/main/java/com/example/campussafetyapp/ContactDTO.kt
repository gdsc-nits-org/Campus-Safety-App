package com.example.campussafetyapp

import android.graphics.Bitmap

data class ContactDTO(
    var name: String = "",
    var photoUri: String? = null,
    var image: Bitmap? = null,
    var isHeader: Boolean = false
)