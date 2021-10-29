package com.dmadunts.samples.infinsample.di

import com.dmadunts.samples.infinsample.screens.login.LoginViewModel
import com.dmadunts.samples.infinsample.screens.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SearchViewModel() }
}
