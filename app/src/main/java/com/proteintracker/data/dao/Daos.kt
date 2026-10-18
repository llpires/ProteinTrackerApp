package com.proteintracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.proteintracker.data.model.Meal
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAll(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    suspend fun getAllSync(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE isQuick = 1 ORDER BY prepTimeMinutes ASC")
    fun getQuickRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE isMealPrep = 1 ORDER BY name ASC")
    fun getMealPrepRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' OR ingredients LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getById(id: Long): Recipe?

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int
}

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<Meal>)

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("DELETE FROM meals WHERE date = :date AND mealType = :mealType AND foodName = :foodName")
    suspend fun deleteByDetails(date: Long, mealType: Int, foodName: String)

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY mealType ASC, timestamp ASC")
    fun getMealsByDate(date: Long): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY mealType ASC, timestamp ASC")
    suspend fun getMealsByDateSync(date: Long): List<Meal>

    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC, mealType ASC")
    fun getMealsByDateRange(startDate: Long, endDate: Long): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC, mealType ASC")
    suspend fun getMealsByDateRangeSync(startDate: Long, endDate: Long): List<Meal>

    @Query("SELECT * FROM meals WHERE recipeId = :recipeId")
    suspend fun getByRecipeId(recipeId: Long): List<Meal>

    @Query("SELECT SUM(proteinGrams) FROM meals WHERE date = :date")
    suspend fun getTotalProteinByDate(date: Long): Double?

    @Query("SELECT COUNT(DISTINCT date) FROM meals")
    suspend fun getTrackedDaysCount(): Int
}

@Dao
interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: UserSettings)

    @Update
    suspend fun update(settings: UserSettings)

    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getSettings(): Flow<UserSettings?>

    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getSettingsSync(): UserSettings?
}