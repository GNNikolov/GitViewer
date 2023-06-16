package com.gnnikolov.gitviewer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gnnikolov.gitviewer.ui.viewmodel.CommitsViewModel
import com.gnnikolov.gitviewer.ui.viewmodel.GitHubRepoModelsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GitHubRepoModelsViewModel::class)
    abstract fun gitHubRepoViewModel(viewModel: GitHubRepoModelsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommitsViewModel::class)
    abstract fun commitsViewModel(viewModel: CommitsViewModel): ViewModel
}