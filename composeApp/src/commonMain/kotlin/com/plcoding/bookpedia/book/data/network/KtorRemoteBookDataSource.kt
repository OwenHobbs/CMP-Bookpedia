package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"
private const val BOOK_LANGUAGE = "eng"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
): RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        resultLimt: Int?
    ): Result<SearchResponseDto, DataError.Remote> {
        // TODO: how does this give a SearchResponseDto?
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/search.json"
            ) {
                parameter("q", query)
                parameter("limit", resultLimt)
                parameter("language", BOOK_LANGUAGE)
                parameter("fields", "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count")
            }
        }
    }
}