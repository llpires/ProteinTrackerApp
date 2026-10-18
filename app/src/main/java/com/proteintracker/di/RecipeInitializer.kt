package com.proteintracker.di

import android.content.Context
import com.proteintracker.data.repository.Repository
import com.proteintracker.util.RecipeDataLoader
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RecipeInitializer {
    @Inject
    fun initializeRecipes()
}

class RecipeInitializerImpl @Inject constructor(
    private val context: Context,
    private val repository: Repository
) : RecipeInitializer {
    override fun initializeRecipes() {
        RecipeDataLoader.loadRecipesIfNeeded(context, repository)
    }
}