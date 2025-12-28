package com.plcoding.bookpedia.book.presentation

import androidx.lifecycle.ViewModel
import com.plcoding.bookpedia.book.domain.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// This is a shared view model to share relevant data
// between book list and book detail screens
class SelectedBookViewModel: ViewModel() {

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook = _selectedBook.asStateFlow()

    fun onSelectedBook(book: Book?) {
        _selectedBook.value = book
    }

}