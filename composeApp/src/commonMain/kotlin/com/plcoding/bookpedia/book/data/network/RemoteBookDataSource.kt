package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

// This abstraction may not be necessary
// Could be useful if changing HTTP client later
interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimt: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>
}