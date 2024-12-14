package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.domain.SearchedBooks
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {

    private val searchedBooks = SearchedBooks(0, null)
    private var searchJob: Job? = null
    private var favoriteJob: Job? = null

    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if (searchedBooks.books == null) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private fun observeFavoriteBooks() {
        favoriteJob?.cancel()
        favoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update {
                    it.copy(
                        favoriteBooks = favoriteBooks
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResultsCount = 0,
                                searchResults = null
                            )
                        }
                    }
                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = true,
                searchResultsCount = 0,
                searchResults = null
            )
        }

        bookRepository
            .searchBooks(query.trim())
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        loading = false,
                        searchResultsCount = result.booksCount,
                        searchResults = result.books
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        searchResultsCount = 0,
                        searchResults = emptyList(),
                        loading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

    fun onAction(action: BookListAction) {
        when(action) {
            is BookListAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    if (action.favorite) {
                        bookRepository.deleteFromFavorites(action.book.id)
                    } else {
                        bookRepository.markAsFavorite(action.book)
                    }
                }
            }
            BookListAction.OnSearchActiveChange -> {
                _state.update {
                    it.copy(searchActive = !it.searchActive)
                }
            }
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            else -> Unit
        }
    }
}