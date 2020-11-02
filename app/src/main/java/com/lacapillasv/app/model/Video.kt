package com.lacapillasv.app.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Video(
    var title: String = "",
    @get:PropertyName("sub_title")
    @set:PropertyName("sub_title")
    var subTitle: String = "",
    var url: String = "",
    @Exclude
    var releaseDate: Date? = null
) : DataClassModel()