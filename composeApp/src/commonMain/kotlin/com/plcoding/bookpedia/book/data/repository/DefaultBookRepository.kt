package com.plcoding.bookpedia.book.data.repository

import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map

// Repositories are helpful for combining multiple data sources
// Such as local and remote data sources
// Repositories return domain models (convert DTO to domain model)
class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(
                query = query,
                // resultLimt = 20
            )
            // TODO: review how this map function works
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }
}