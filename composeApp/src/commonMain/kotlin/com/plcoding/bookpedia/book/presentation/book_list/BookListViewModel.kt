package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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

// Presentation -> Domain <- Data
class BookListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {

    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null // TODO: review
    private var observeFavoriteBooksJob: Job? = null

    // TODO: What is MutableStateFlow? Why not MutableState?
    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            // This could instead be in an init block
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            // When navigating to book detail screen this view model will stay active
            // This onStart will run when view model is created and
            // when navigating back from book detail screen.
            // So we need to ensure previous job is cancelled
            // Alternatively, we could use init block
            observeFavoriteBooks()
        }
        .stateIn( // TODO: REVIEW!
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookListAction) {
        when(action) {
            is BookListAction.OnBookClick -> {

            }
            is BookListAction.OnSearchQueryChange -> {
                // update state in threadsafe manner to avoid race conditions
                if (action.isAcknowledge) { // TODO: hacky fix
                    _state.update {
                        it.copy(
                            isSearchQueryUpdated = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            searchQuery = action.query,
                            isSearchQueryUpdated = true
                        )
                    }
                }
            }
            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavoriteBooksJob?.cancel()
        observeFavoriteBooksJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update { it.copy(
                    favoriteBooks = favoriteBooks
                ) }
            }
            .launchIn(viewModelScope)
    }

    // In order to trigger search, we need to listen to state changes of our search query field
    private fun observeSearchQuery() {
        // TODO: review flow chain stuff
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    // If we cleared the search query, just show the last displayed books
                    query.isBlank() -> {
                        _state.update { it.copy(
                            errorMessage = null,
                            searchResults = cachedBooks
                        ) }
                    }
                    // Only trigger search if there are two or more characters
                    query.length >= 2 -> {
                        // Cancel previous search
                        searchJob?.cancel()
                        // Start a new one
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope) // TODO: review
    }

    private fun searchBooks(query: String) =
        viewModelScope.launch { // TODO: review
            _state.update { it.copy(
                isLoading = true
            ) }
            bookRepository
                .searchBooks(query)
                .onSuccess { searchResults ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText(),
                        searchResults = emptyList()
                    ) }
                }
        }

}