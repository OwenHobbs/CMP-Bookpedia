package com.plcoding.bookpedia

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.plcoding.bookpedia.app.App
import com.plcoding.bookpedia.di.initKoin

fun main() {
    initKoin() // we don't need any extra config for desktop right now
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CMP-Bookpedia",
        ) {
            App()
        }
    }
}