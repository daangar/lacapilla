package com.lacapillasv.app.ui.readingclub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lacapillasv.app.model.Guide
import kotlinx.coroutines.launch

class GuidesViewModel: ViewModel() {

    private lateinit var _bookId: String
    private val repository = ReadingClubRepository()


    private val _guides = MutableLiveData<List<Guide>>()
    val guides: LiveData<List<Guide>> = _guides

    private val _loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loading: LiveData<Boolean> = _loading

    fun setBookId(id: String) {
        _bookId = id
    }

    fun showGuides() {
        showLoading()
        viewModelScope.launch {
            val guides = repository.getGuides(_bookId)
            _guides.value = guides
            hideLoading()
        }
    }

    private fun showLoading() {
        _loading.value = true
    }

    private fun hideLoading() {
        _loading.value = false
    }
}