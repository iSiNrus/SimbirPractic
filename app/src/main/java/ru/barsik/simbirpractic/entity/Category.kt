package ru.barsik.simbirpractic.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id : Int,
    val title : String,
    @SerializedName("icon_path")
    val iconPath: String
) : Parcelable