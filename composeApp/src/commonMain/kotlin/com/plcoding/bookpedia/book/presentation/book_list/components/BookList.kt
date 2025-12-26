package com.plcoding.bookpedia.book.presentation.book_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.book.domain.Book
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BookList(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = books,
            key = { it.id } // for optimizing performance and animations
        ) { book ->
            BookListItem(
                book = book,
                modifier = Modifier
                    .widthIn(max = 700.dp) // primarily for desktop
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    onBookClick(book)
                }
            )
        }
    }
}

@Preview
@Composable
private fun BookListPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            BookList(
                books = listOf(
                    Book(
                        id = "myBook",
                        title = "Hello World",
                        imageUrl = "",
                        authors = listOf("Owen"),
                        description = "This is my new book.",
                        languages = listOf("English", "Deutsch"),
                        firstPublishYear = "1998",
                        averageRating = 6.1,
                        ratingCount = 12,
                        numPages = 142,
                        numEditions = 3
                    ),
                    Book(
                        id = "myBook2",
                        title = "My Second Book",
                        imageUrl = "",
                        authors = listOf("O Hobbs"),
                        description = null,
                        languages = listOf("English", "Deutsch"),
                        firstPublishYear = "1998",
                        averageRating = 3.19,
                        ratingCount = 12,
                        numPages = 142,
                        numEditions = 3
                    ),
                    Book(
                        id = "myBook3",
                        title = "My Third Book",
                        imageUrl = "",
                        authors = listOf(),
                        description = null,
                        languages = listOf("English", "Deutsch"),
                        firstPublishYear = "1998",
                        averageRating = null,
                        ratingCount = 12,
                        numPages = 142,
                        numEditions = 3
                    )
                ),
                onBookClick = { book ->

                }
            )
        }
    }
}