package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import org.koin.compose.viewmodel.koinViewModel

// put this composable in the nav host
@Composable
fun BookListScreenRoot(
    // use dependency injection
    viewModel: BookListViewModel = koinViewModel(),
    // this lambda has impact on navigating
    onBookClick: (Book) -> Unit
) {
    // TODO: what does this mean?
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListScreen(
        state = state,
        // viewModel does not have access to nav controller
        // onAction = viewModel::onAction
        // so instead intercept actions that require nav controller
        onAction = { action ->
            when(action) {
                // this action is not handled in the view model
                // since we don't have access to nav controller there
                // so pass it to lambda
                is BookListAction.OnBookClick -> onBookClick(action.book)
                else -> Unit
            }
            // forward other actions to viewModel
            viewModel.onAction(action)
        }
    )
}

// We don't want this function to rely on a view model reference
// so we can use it in basic previews / UI test
@Composable
private fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BookListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp) // max width important for desktop
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}