package com.example.campussafetyapp

import android.graphics.Bitmap

data class ContactDTO(var name: String = "", var number: String = "", var image: Bitmap? = null, var photoUri: String? = null)
