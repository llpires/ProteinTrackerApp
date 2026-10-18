package com.proteintracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proteintracker.data.repository.Repository
import com.proteintracker.viewmodel.HomeViewModel
import com.proteintracker.viewmodel.LoggerViewModel
import com.proteintracker.viewmodel.ProgressViewModel
import com.proteintracker.viewmodel.RecipesViewModel
import com.proteintracker.viewmodel.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Suppress("UNUSED_PARAMETER")
    @Binds
    abstract fun bindHomeViewModel(impl: HomeViewModelImpl): HomeViewModel

    @Suppress("UNUSED_PARAMETER")
    @Binds
    abstract fun bindLoggerViewModel(impl: LoggerViewModelImpl): LoggerViewModel

    @Suppress("UNUSED_PARAMETER")
    @Binds
    abstract fun bindRecipesViewModel(impl: RecipesViewModelImpl): RecipesViewModel

    @Suppress("UNUSED_PARAMETER")
    @Binds
    abstract fun bindProgressViewModel(impl: ProgressViewModelImpl): ProgressViewModel

    @Suppress("UNUSED_PARAMETER")
    @Binds
    abstract fun bindSettingsViewModel(impl: SettingsViewModelImpl): SettingsViewModel
}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelFactoryModule {
    @Provides
    @Singleton
    fun provideHomeViewModel(repository: Repository): HomeViewModel = HomeViewModelImpl(repository)

    @Provides
    @Singleton
    fun provideLoggerViewModel(repository: Repository): LoggerViewModel = LoggerViewModelImpl(repository)

    @Provides
    @Singleton
    fun provideRecipesViewModel(repository: Repository): RecipesViewModel = RecipesViewModelImpl(repository)

    @Provides
    @Singleton
    fun provideProgressViewModel(repository: Repository): ProgressViewModel = ProgressViewModelImpl(repository)

    @Provides
    @Singleton
    fun provideSettingsViewModel(repository: Repository): SettingsViewModel = SettingsViewModelImpl(repository)
}

// Implementation classes to allow Hilt binding
class HomeViewModelImpl @Inject constructor(repository: Repository) : HomeViewModel(repository)
class LoggerViewModelImpl @Inject constructor(repository: Repository) : LoggerViewModel(repository)
class RecipesViewModelImpl @Inject constructor(repository: Repository) : RecipesViewModel(repository)
class ProgressViewModelImpl @Inject constructor(repository: Repository) : ProgressViewModel(repository)
class SettingsViewModelImpl @Inject constructor(repository: Repository) : SettingsViewModel(repository)