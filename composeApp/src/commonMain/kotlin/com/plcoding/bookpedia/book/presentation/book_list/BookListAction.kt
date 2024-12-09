package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data object OnSearchActiveChange: BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data class OnFavoriteClick(val book: Book): BookListAction
}