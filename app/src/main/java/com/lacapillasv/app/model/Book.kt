package com.lacapillasv.app.model

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName

data class Book (
    var name: String = "",
    @Exclude
    var type: BookType = BookType.UNKNOWN
) : DataClassModel()