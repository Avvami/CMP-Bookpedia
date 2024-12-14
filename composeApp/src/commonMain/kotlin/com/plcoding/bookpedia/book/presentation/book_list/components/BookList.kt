package com.plcoding.bookpedia.book.presentation.book_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.book.domain.Book

@Composable
fun BookList(
    modifier: Modifier = Modifier,
    booksCount: Int = 0,
    books: List<Book>,
    favorites: List<Book>,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book, favorite: Boolean) -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 400.dp),
        modifier = modifier,
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
    ) {
        if (booksCount != 0) {
            item(
                span = { GridItemSpan(1) }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (booksCount == 1) "$booksCount result" else "$booksCount results",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        items(
            items = books,
            key = { it.id }
        ) { book ->
            val favorite = favorites.any { it.id == book.id }
            BookCard(
                modifier = Modifier.fillMaxWidth(),
                book = book,
                isFavorite = favorite,
                onClick = { onBookClick(book) },
                onFavoriteClick = { onFavoriteClick(it, favorite) }
            )
        }
    }
}