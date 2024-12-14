package com.plcoding.bookpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.app.App
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailScreen
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailState
import com.plcoding.bookpedia.book.presentation.book_list.components.BookCard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    BookCard(
        modifier = Modifier.fillMaxWidth(),
        book = Book(
            id = "id",
            title = "Harry Potter and the Chamber of Secrets",
            imageUrl = "",
            description = "",
            authors = listOf("J. K. Rawling"),
            languages = emptyList(),
            firstPublishYear = null,
            averageRating = 4.6,
            ratingCount = 402,
            pages = 312,
            editions = 2
        ),
        isFavorite = false,
        onClick = { /*TODO*/ },
        onFavoriteClick = { /*TODO*/ }
    )
}

val books = (1..100).map {
    Book(
        id = it.toString(),
        title = "Booked book",
        imageUrl = "",
        description = "",
        authors = listOf("J. K. Rawling"),
        languages = listOf("eng", "sp"),
        firstPublishYear = null,
        averageRating = 3.2345,
        ratingCount = 130,
        pages = 356,
        editions = 1
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    BookDetailScreen(
        state = BookDetailState(
            book = books.first(),
            favorite = true
        ),
        onAction = { /*TODO*/ }
    )
}