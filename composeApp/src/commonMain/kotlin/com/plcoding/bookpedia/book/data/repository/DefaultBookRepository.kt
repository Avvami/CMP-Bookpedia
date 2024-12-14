package com.plcoding.bookpedia.book.data.repository

import com.plcoding.bookpedia.book.data.mappers.toSearchedBooks
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.domain.SearchedBooks
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource
): BookRepository {
    override suspend fun searchBooks(query: String): Result<SearchedBooks, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { it.toSearchedBooks() }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        return remoteBookDataSource
            .getBookDetails(bookId)
            .map { it.description }
//        val localResult = favoriteBookDao.getFavoriteBook(bookId)
//
//        return if(localResult == null) {
//        } else {
//            Result.Success(localResult.description)
//        }
    }
}