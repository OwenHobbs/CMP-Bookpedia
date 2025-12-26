package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

// Use Model View Intent (MVI)
data class BookListState(
    val searchQuery: String = "",
    val searchResults: List<Book> = dummyBooks, // TODO: make emptyList()
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)

private val dummyBooks = (1..100).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "",
        authors = listOf("Owen Hobbs"),
        description = "Description $it",
        languages = emptyList(),
        firstPublishYear = null,
        averageRating = 4.67854,
        ratingCount = 5,
        numPages = 100,
        numEditions = 3
    )
}