package com.lacapillasv.app.model

import com.google.firebase.firestore.Exclude

open class DataClassModel {
    @Exclude
    var id: String = ""

    @Suppress("UNCHECKED_CAST")
    fun <T : DataClassModel?> withId(id: String): T {
        this.id = id
        return this as T
    }
}