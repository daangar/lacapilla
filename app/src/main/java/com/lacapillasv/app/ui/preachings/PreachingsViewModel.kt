package com.lacapillasv.app.ui.preachings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreachingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Preachings"
    }
    val text: LiveData<String> = _text
}