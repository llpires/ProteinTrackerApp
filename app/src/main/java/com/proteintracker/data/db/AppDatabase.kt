package com.proteintracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.proteintracker.data.dao.MealDao
import com.proteintracker.data.dao.RecipeDao
import com.proteintracker.data.dao.UserSettingsDao
import com.proteintracker.data.model.Meal
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.UserSettings

@Database(
    entities = [Recipe::class, Meal::class, UserSettings::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealDao(): MealDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "protein_tracker_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}