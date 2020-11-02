package com.lacapillasv.app.model

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import java.util.*


data class Guide (
    var name: String = "",
    @SerializedName("file_reference")
    var fileReference: String = "",
    @Exclude
    var releaseDate: Date? = null
) : DataClassModel()