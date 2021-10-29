package com.dmadunts.samples.infinsample.di

import com.dmadunts.samples.infinsample.remote.repositories.implementation.GitHubRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single { GitHubRepositoryImpl(get()) }

}