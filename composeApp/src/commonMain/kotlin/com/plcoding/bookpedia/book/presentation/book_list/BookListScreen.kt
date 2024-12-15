package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.app_name
import cmp_bookpedia.composeapp.generated.resources.no_favorite_books
import cmp_bookpedia.composeapp.generated.resources.no_search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.SearchTextField
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is BookListAction.OnBookClick -> onBookClick(action.book)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchResultsGridState = rememberLazyGridState()
    val favoritesGridState = rememberLazyGridState()

    LaunchedEffect(state.searchResults) {
        searchResultsGridState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = state.searchActive,
                        label = "Text Filed Animation",
                        transitionSpec = {
                            fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith
                                    fadeOut(animationSpec = tween(durationMillis = 100))
                        }
                    ) { targetState ->
                        if (targetState) {
                            SearchTextField(
                                modifier = Modifier.fillMaxWidth(),
                                searchQuery = state.searchQuery,
                                onSearchQueryChange = { query ->
                                    onAction(BookListAction.OnSearchQueryChange(query))
                                },
                                onImeSearch = {
                                    keyboardController?.hide()
                                }
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.app_name)
                            )
                        }
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = state.searchActive,
                        enter = expandHorizontally() + fadeIn(),
                        exit = shrinkHorizontally() + fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                onAction(BookListAction.OnSearchActiveChange)
                                onAction(BookListAction.OnSearchQueryChange(""))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(visible = !state.searchActive) {
                        IconButton(
                            onClick = { onAction(BookListAction.OnSearchActiveChange) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        AnimatedContent(
            modifier = Modifier.padding(innerPadding),
            targetState = state.searchActive,
            transitionSpec = { fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith fadeOut(animationSpec = tween(durationMillis = 100)) }
        ) { targetState ->
            if (targetState) {
                if (state.loading && state.searchResults == null) {

                } else {
                    when {
                        state.errorMessage != null -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                text = state.errorMessage.asString(),
                                color = MaterialTheme.colorScheme.onSurface.copy(.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {
                            state.searchResults?.let { results ->
                                if (results.isEmpty()) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        text = stringResource(Res.string.no_search_results),
                                        color = MaterialTheme.colorScheme.onSurface.copy(.7f),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    BookList(
                                        booksCount = state.searchResultsCount,
                                        books = results,
                                        favorites = state.favoriteBooks,
                                        onBookClick = { book -> onAction(BookListAction.OnBookClick(book)) },
                                        onFavoriteClick = { book, favorite -> onAction(BookListAction.OnFavoriteClick(book, favorite)) },
                                        lazyGridState = searchResultsGridState
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                if (state.favoriteBooks.isEmpty()) {
                    val parts = stringResource(Res.string.no_favorite_books).split("{icon}")
                    val annotatedString = buildAnnotatedString {
                        append(parts[0])
                        appendInlineContent("icon", "[icon]")
                        append(parts[1])
                    }
                    val inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(
                                width = 18.sp,
                                height = 18.sp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = annotatedString,
                        inlineContent = inlineContent,
                        color = MaterialTheme.colorScheme.onSurface.copy(.7f),
                        textAlign = TextAlign.Center
                    )
                } else {
                    BookList(
                        books = state.favoriteBooks,
                        favorites = state.favoriteBooks,
                        onBookClick = { book -> onAction(BookListAction.OnBookClick(book)) },
                        onFavoriteClick = { book, favorite -> onAction(BookListAction.OnFavoriteClick(book, favorite)) },
                        lazyGridState = favoritesGridState
                    )
                }
            }
        }
    }
}