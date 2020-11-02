package com.lacapillasv.app.ui.readingclub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lacapillasv.app.model.Book
import com.lacapillasv.app.model.BookType
import kotlinx.coroutines.launch

class ReadingClubViewModel : ViewModel() {

    private val repository = ReadingClubRepository()

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loading: LiveData<Boolean> = _loading

    fun showBibbleBooks() {
        _books.value = listOf()
        showLoading()
        viewModelScope.launch {
            val bibleBooks = repository.getBooks(BookType.BIBLE)
            _books.value = bibleBooks
            hideLoading()
        }
    }

    fun showRelatedBooks() {
        _books.value = listOf()
        showLoading()
        viewModelScope.launch {
            val relatedBooks = repository.getBooks(BookType.RELATED)
            _books.value = relatedBooks
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