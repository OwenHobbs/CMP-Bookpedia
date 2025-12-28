package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// Data Access Object
// Outlines functions to interact with our database
@Dao
interface FavoriteBookDao {

    // insert book if it doesn't exist
    // update book if it already exists
    @Upsert
    suspend fun upsert(book: BookEntity)

    // We want a function that automatically triggers when we change
    // something about the query (adding new favorite, deleting, etc.)
    // so we wrap it in a kotlin Flow which does not do anything until
    // it is launched in a coroutine scope, so function is not suspend
    @Query("SELECT * FROM BookEntity")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getFavoriteBook(id: String): BookEntity?

    @Query("DELETE FROM BookEntity WHERE id = :id")
    suspend fun deleteFavoriteBook(id: String)

}