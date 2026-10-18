package com.proteintracker.di

import android.content.Context
import com.proteintracker.data.db.AppDatabase
import com.proteintracker.data.dao.MealDao
import com.proteintracker.data.dao.RecipeDao
import com.proteintracker.data.dao.UserSettingsDao
import com.proteintracker.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@Suppress("UNUSED_PARAMETER") context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRecipeDao(database: AppDatabase): RecipeDao = database.recipeDao()

    @Provides
    @Singleton
    fun provideMealDao(database: AppDatabase): MealDao = database.mealDao()

    @Provides
    @Singleton
    fun provideSettingsDao(database: AppDatabase): UserSettingsDao = database.userSettingsDao()
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        recipeDao: RecipeDao,
        mealDao: MealDao,
        settingsDao: UserSettingsDao
    ): Repository = Repository(recipeDao, mealDao, settingsDao)
}