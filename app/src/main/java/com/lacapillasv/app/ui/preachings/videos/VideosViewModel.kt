package com.lacapillasv.app.ui.preachings.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lacapillasv.app.model.Video
import kotlinx.coroutines.launch

class VideosViewModel: ViewModel() {
    private val repository = VideosRepository()

    private val _videos = MutableLiveData<List<Video>>()
    fun showVideos(): LiveData<List<Video>> = _videos

    private val _loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loading: LiveData<Boolean> = _loading

    private fun showLoading() {
        _loading.value = true
    }

    private fun hideLoading() {
        _loading.value = false
    }

    fun getVideos() {
        _videos.value = listOf()
        showLoading()
        viewModelScope.launch {
            val videos = repository.getVideos()
            _videos.value = videos
            hideLoading()
        }
    }
}