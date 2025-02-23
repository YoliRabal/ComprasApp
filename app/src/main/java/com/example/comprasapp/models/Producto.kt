package com.example.comprasapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Producto(
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String
) : Parcelable
