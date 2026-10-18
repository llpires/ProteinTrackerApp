package com.proteintracker.util

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.proteintracker.data.repository.Repository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.entryPoints
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.EntryPointAccessors.fromApplication

@HiltAndroidApp
class ProteinTrackerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize recipes in background
        val entryPoint = fromApplication(this, RecipeInitializer::class.java)
        entryPoint.initializeRecipes()
    }
}

interface RecipeInitializer : dagger.hilt.EntryPoint {
    fun initializeRecipes()
}