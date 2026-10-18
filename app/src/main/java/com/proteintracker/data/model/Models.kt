package com.proteintracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.proteintracker.data.db.Converters
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    val category: RecipeCategory,
    val proteinPerServing: Double,
    val caloriesPerServing: Int,
    val carbsPerServing: Double,
    val fatPerServing: Double,
    val prepTimeMinutes: Int,
    val servings: Int,
    val ingredients: String, // JSON array stored as string
    val instructions: String, // JSON array stored as string
    val tips: String,
    val isMealPrep: Boolean = false,
    val isQuick: Boolean = false, // < 15 min
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class RecipeCategory(val displayName: String) {
    SAVORY("Salgada"),
    SWEET("Doce"),
    BOTH("Mista")
}

@Serializable
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val date: Long, // LocalDate.toEpochDays()
    val mealType: MealType,
    val foodName: String,
    val proteinGrams: Double,
    val calories: Int = 0,
    val carbsGrams: Double = 0.0,
    val fatGrams: Double = 0.0,
    val recipeId: Long? = null, // Link to recipe if from recipe
    val timestamp: Long = System.currentTimeMillis()
)

enum class MealType(val displayName: String, val order: Int, val colorRes: Int) {
    BREAKFAST("Café da manhã", 0, 0xFFFFB74D),
    MORNING_SNACK("Lanche manhã", 1, 0xFFA5D6A7),
    LUNCH("Almoço", 2, 0xFF4FC3F7),
    PRE_WORKOUT("Pré-treino", 3, 0xFFFF8A65),
    POST_WORKOUT("Pós-treino", 4, 0xFFFF8A65),
    DINNER("Jantar", 5, 0xFF9575CD),
    EVENING_SNACK("Ceia", 6, 0xFFA5D6A7)
}

@Serializable
data class DayLog(
    val date: LocalDate,
    val meals: List<Meal> = emptyList(),
    val targetProtein: Double = 190.0
) {
    val totalProtein: Double get() = meals.sumOf { it.proteinGrams }
    val totalCalories: Int get() = meals.sumOf { it.calories }
    val totalCarbs: Double get() = meals.sumOf { it.carbsGrams }
    val totalFat: Double get() = meals.sumOf { it.fatGrams }
    
    val remainingProtein: Double get() = maxOf(0.0, targetProtein - totalProtein)
    val progressPercent: Double get() = (totalProtein / targetProtein * 100).coerceAtMost(100.0)
    val isTargetMet: Boolean get() = totalProtein >= targetProtein
    
    fun mealsByType(): Map<MealType, List<Meal>> {
        return meals.groupBy { it.mealType }
    }
}

@Serializable
@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey val id: Int = 1, // Singleton
    val targetWeightKg: Double = 95.0,
    val currentWeightKg: Double = 107.0,
    val proteinPerKg: Double = 2.0,
    val calculatedDailyTarget: Double = 190.0,
    val mealRemindersEnabled: Boolean = true,
    val reminderTimes: String = "07:00,10:00,12:30,16:00,18:30,21:00", // CSV
    val themeMode: Int = 0, // 0=system, 1=light, 2=dark
    val notificationsEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun reminderTimesList(): List<String> = reminderTimes.split(",").map { it.trim() }
}

@Serializable
data class WeeklyStats(
    val weekStart: LocalDate,
    val dailyLogs: List<DayLog>,
    val averageProtein: Double,
    val daysOnTarget: Int,
    val bestStreak: Int,
    val currentStreak: Int,
    val consistencyPercent: Double
)

@Serializable
data class MonthlyStats(
    val year: Int,
    val month: Int,
    val dailyLogs: List<DayLog>,
    val averageProtein: Double,
    val daysOnTarget: Int,
    val totalDaysTracked: Int
)