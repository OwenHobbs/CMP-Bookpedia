package com.plcoding.bookpedia.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this) // TODO: review extension functions with invoke
        modules(sharedModule, platformModule)
    }
}