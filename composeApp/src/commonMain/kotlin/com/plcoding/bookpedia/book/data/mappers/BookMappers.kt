package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

// This file is responsible for mapping data models to domain models
// It uses extension functions

fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id,
        title = title,
        // TODO: define URL as constant?
        imageUrl = if (coverKey != null) "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
            else "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg",
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
    )
}