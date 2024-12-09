package com.plcoding.bookpedia.book.domain

data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String?,
    val authors: List<String>,
    val languages: List<String>,
    val firstPublishYear: String?,
    val averageRating: Double?,
    val ratingCount: Int?,
    val pages: Int,
    val editions: Int
)
