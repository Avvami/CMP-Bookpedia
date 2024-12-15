package com.plcoding.bookpedia.book.domain

data class SearchedBooks(
    val booksCount: Int,
    val books: List<Book>?
)
