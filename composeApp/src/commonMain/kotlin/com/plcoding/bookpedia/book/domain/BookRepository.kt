package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface BookRepository {
    suspend fun searchBooks(query: String): Result<SearchedBooks, DataError.Remote>

    suspend fun getBookDescription(bookId: String): Result<String?, DataError>
}