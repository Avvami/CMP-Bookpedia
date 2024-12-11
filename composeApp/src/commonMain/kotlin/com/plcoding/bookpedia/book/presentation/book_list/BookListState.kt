package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "",
    val searchActive: Boolean = false,
    val searchResultsCount: Int = 0,
    val searchResults: List<Book>? = null,
    val favoriteBooks: List<Book> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
