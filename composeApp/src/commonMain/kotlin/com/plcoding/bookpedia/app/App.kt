package com.plcoding.bookpedia.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailScreenRoot
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        // TODO: review this navigation stuff!
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ) {
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {
                composable<Route.BookList> {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    // Runs whenever we are on this screen
                    LaunchedEffect(true) { // TODO: why is this necessary?
                        selectedBookViewModel.onSelectedBook(null)
                    }

                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            selectedBookViewModel.onSelectedBook(book)
                            navController.navigate(
                                Route.BookDetail(book.id) // TODO: is this parameter still necessary with the shared view model?
                            )
                        }
                    )
                }
                composable<Route.BookDetail> {
                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val selectedBookViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedBook) {
                        selectedBook?.let { // only run if not null
                            viewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }

                    BookDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp() // go to previous screen
                            // TODO: state is remembered except for book list scroll state?
                        }
                    )
                }
            }
        }
    }
}

// TODO: review how this utility function works
@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel (
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}