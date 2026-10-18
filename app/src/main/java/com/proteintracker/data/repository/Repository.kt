package com.proteintracker.data.repository

import com.proteintracker.data.dao.MealDao
import com.proteintracker.data.dao.RecipeDao
import com.proteintracker.data.dao.UserSettingsDao
import com.proteintracker.data.model.DayLog
import com.proteintracker.data.model.Meal
import com.proteintracker.data.model.MealType
import com.proteintracker.data.model.MonthlyStats
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.UserSettings
import com.proteintracker.data.model.WeeklyStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val recipeDao: RecipeDao,
    private val mealDao: MealDao,
    private val settingsDao: UserSettingsDao
) {
    // Recipe operations
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAll()
    
    suspend fun insertRecipes(recipes: List<Recipe>) = recipeDao.insertAll(recipes)
    
    suspend fun searchRecipes(query: String) = recipeDao.search(query)
    
    suspend fun getRecipesByCategory(category: String) = recipeDao.getByCategory(category)
    
    suspend fun getQuickRecipes() = recipeDao.getQuickRecipes()
    
    suspend fun getMealPrepRecipes() = recipeDao.getMealPrepRecipes()
    
    suspend fun getRecipeById(id: Long) = recipeDao.getById(id)
    
    suspend fun getRecipeCount() = recipeDao.count()

    // Meal operations
    suspend fun logMeal(meal: Meal) = mealDao.insert(meal)
    
    suspend fun updateMeal(meal: Meal) = mealDao.update(meal)
    
    suspend fun deleteMeal(meal: Meal) = mealDao.delete(meal)
    
    suspend fun deleteMealByDetails(date: Long, mealType: Int, foodName: String) = 
        mealDao.deleteByDetails(date, mealType, foodName)
    
    fun getMealsByDate(date: LocalDate): Flow<List<Meal>> = 
        mealDao.getMealsByDate(date.toEpochDays())
    
    suspend fun getMealsByDateSync(date: LocalDate) = 
        mealDao.getMealsByDateSync(date.toEpochDays())
    
    fun getMealsByDateRange(start: LocalDate, end: LocalDate): Flow<List<Meal>> = 
        mealDao.getMealsByDateRange(start.toEpochDays(), end.toEpochDays())
    
    suspend fun getMealsByDateRangeSync(start: LocalDate, end: LocalDate) = 
        mealDao.getMealsByDateRangeSync(start.toEpochDays(), end.toEpochDays())
    
    suspend fun getTotalProteinByDate(date: LocalDate) = 
        mealDao.getTotalProteinByDate(date.toEpochDays()) ?: 0.0
    
    suspend fun getTrackedDaysCount() = mealDao.getTrackedDaysCount()

    // DayLog computation
    fun getDayLog(date: LocalDate): Flow<DayLog> = combine(
        getMealsByDate(date),
        getSettingsFlow()
    ) { meals, settings ->
        DayLog(
            date = date,
            meals = meals,
            targetProtein = settings?.calculatedDailyTarget ?: 190.0
        )
    }
    
    suspend fun getDayLogSync(date: LocalDate): DayLog = withContext(Dispatchers.IO) {
        val meals = mealDao.getMealsByDateSync(date.toEpochDays())
        val settings = settingsDao.getSettingsSync()
        DayLog(
            date = date,
            meals = meals,
            targetProtein = settings?.calculatedDailyTarget ?: 190.0
        )
    }

    // Weekly stats
    fun getWeeklyStats(weekStart: LocalDate): Flow<WeeklyStats> = combine(
        getMealsByDateRange(weekStart, weekStart.plusDays(6)),
        getSettingsFlow()
    ) { meals, settings ->
        val targetProtein = settings?.calculatedDailyTarget ?: 190.0
        val dailyLogs = (0..6).map { offset ->
            val date = weekStart.plusDays(offset)
            val dayMeals = meals.filter { it.date == date.toEpochDays() }
            DayLog(date = date, meals = dayMeals, targetProtein = targetProtein)
        }
        
        val averageProtein = dailyLogs.map { it.totalProtein }.average()
        val daysOnTarget = dailyLogs.count { it.isTargetMet }
        
        // Calculate streaks
        val sortedDates = dailyLogs.filter { it.meals.isNotEmpty() }.map { it.date }.sorted()
        var currentStreak = 0
        var bestStreak = 0
        var tempStreak = 0
        var prevDate: LocalDate? = null
        
        for (date in sortedDates) {
            val dayLog = dailyLogs.first { it.date == date }
            if (dayLog.isTargetMet) {
                tempStreak++
                bestStreak = maxOf(bestStreak, tempStreak)
            } else {
                tempStreak = 0
            }
            prevDate = date
        }
        
        // Current streak (from today backwards)
        val today = Clock.System.now(TimeZone.currentSystemDefault()).toLocalDate()
        for (offset in 0..6) {
            val checkDate = today.minusDays(offset)
            val dayLog = dailyLogs.firstOrNull { it.date == checkDate }
            if (dayLog != null && dayLog.isTargetMet) {
                currentStreak++
            } else if (dayLog != null) {
                break
            }
        }
        
        val consistencyPercent = if (dailyLogs.any { it.meals.isNotEmpty() }) {
            (daysOnTarget.toDouble() / dailyLogs.count { it.meals.isNotEmpty() } * 100)
        } else 0.0
        
        WeeklyStats(
            weekStart = weekStart,
            dailyLogs = dailyLogs,
            averageProtein = averageProtein,
            daysOnTarget = daysOnTarget,
            bestStreak = bestStreak,
            currentStreak = currentStreak,
            consistencyPercent = consistencyPercent
        )
    }

    // Monthly stats
    fun getMonthlyStats(year: Int, month: Int): Flow<MonthlyStats> {
        val start = LocalDate(year, month, 1)
        val end = start.plusMonths(1).minusDays(1)
        return combine(
            getMealsByDateRange(start, end),
            getSettingsFlow()
        ) { meals, settings ->
            val targetProtein = settings?.calculatedDailyTarget ?: 190.0
            val dailyLogs = (1..end.dayOfMonth).map { day ->
                val date = LocalDate(year, month, day)
                val dayMeals = meals.filter { it.date == date.toEpochDays() }
                DayLog(date = date, meals = dayMeals, targetProtein = targetProtein)
            }
            
            val trackedDays = dailyLogs.filter { it.meals.isNotEmpty() }
            val averageProtein = if (trackedDays.isNotEmpty()) {
                trackedDays.map { it.totalProtein }.average()
            } else 0.0
            val daysOnTarget = trackedDays.count { it.isTargetMet }
            
            MonthlyStats(
                year = year,
                month = month,
                dailyLogs = dailyLogs,
                averageProtein = averageProtein,
                daysOnTarget = daysOnTarget,
                totalDaysTracked = trackedDays.size
            )
        }
    }

    // Settings
    val settingsFlow: Flow<UserSettings?> = settingsDao.getSettings()
    
    fun getSettingsFlow(): Flow<UserSettings?> = settingsDao.getSettings()
    
    suspend fun getSettingsSync() = settingsDao.getSettingsSync()
    
    suspend fun updateSettings(settings: UserSettings) = settingsDao.update(
        settings.copy(updatedAt = System.currentTimeMillis())
    )
    
    suspend fun updateTargetWeight(targetWeight: Double) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        val newTarget = (targetWeight * current.proteinPerKg).roundToInt().toDouble()
        settingsDao.update(current.copy(
            targetWeightKg = targetWeight,
            calculatedDailyTarget = newTarget,
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun updateProteinPerKg(proteinPerKg: Double) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        val newTarget = (current.targetWeightKg * proteinPerKg).roundToInt().toDouble()
        settingsDao.update(current.copy(
            proteinPerKg = proteinPerKg,
            calculatedDailyTarget = newTarget,
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun updateCurrentWeight(weight: Double) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        settingsDao.update(current.copy(
            currentWeightKg = weight,
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun toggleReminders(enabled: Boolean) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        settingsDao.update(current.copy(
            mealRemindersEnabled = enabled,
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun updateReminderTimes(times: List<String>) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        settingsDao.update(current.copy(
            reminderTimes = times.joinToString(","),
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun toggleNotifications(enabled: Boolean) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        settingsDao.update(current.copy(
            notificationsEnabled = enabled,
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    suspend fun updateThemeMode(mode: Int) = withContext(Dispatchers.IO) {
        val current = settingsDao.getSettingsSync() ?: UserSettings()
        settingsDao.update(current.copy(
            themeMode = mode,
            updatedAt = System.currentTimeMillis()
        ))
    }
}