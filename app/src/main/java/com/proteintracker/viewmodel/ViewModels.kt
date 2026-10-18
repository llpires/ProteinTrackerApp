package com.proteintracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteintracker.data.model.DayLog
import com.proteintracker.data.model.Meal
import com.proteintracker.data.model.MealType
import com.proteintracker.data.model.MonthlyStats
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.UserSettings
import com.proteintracker.data.model.WeeklyStats
import com.proteintracker.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _today = MutableStateFlow(LocalDate.now())
    val today: StateFlow<LocalDate> = _today
    
    val dayLog = repository.getDayLog(_today.value)
    
    val settings = repository.settingsFlow
    
    val nextMealType: StateFlow<MealType?> = combine(dayLog, settings) { log, settings ->
        val mealTypes = MealType.values().sortedBy { it.order }
        val targetProtein = settings?.calculatedDailyTarget ?: 190.0
        val perMealTarget = targetProtein / mealTypes.size
        
        mealTypes.firstOrNull { type ->
            log.mealsByType()[type]?.sumOf { it.proteinGrams } ?: 0.0 < perMealTarget
        }
    }
    
    fun refreshToday() {
        _today.value = Clock.System.now(TimeZone.currentSystemDefault()).toLocalDate()
    }
    
    fun logMeal(meal: Meal) {
        viewModelScope.launch {
            repository.logMeal(meal)
        }
    }
    
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }
}

class LoggerViewModel(private val repository: Repository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    private val _selectedMealType = MutableStateFlow<MealType>(MealType.BREAKFAST)
    val selectedMealType: StateFlow<MealType> = _selectedMealType
    
    val recipes = repository.allRecipes
    
    val filteredRecipes = combine(recipes, _searchQuery) { recipes, query ->
        if (query.isBlank()) recipes else recipes.filter { it.name.contains(query, ignoreCase = true) }
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setMealType(type: MealType) {
        _selectedMealType.value = type
    }
    
    fun logRecipe(recipe: Recipe, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            val meal = Meal(
                date = date.toEpochDays(),
                mealType = _selectedMealType.value,
                foodName = recipe.name,
                proteinGrams = recipe.proteinPerServing,
                calories = recipe.caloriesPerServing,
                carbsGrams = recipe.carbsPerServing,
                fatGrams = recipe.fatPerServing,
                recipeId = recipe.id
            )
            repository.logMeal(meal)
        }
    }
    
    fun logCustomMeal(
        foodName: String,
        protein: Double,
        calories: Int = 0,
        carbs: Double = 0.0,
        fat: Double = 0.0,
        date: LocalDate = LocalDate.now()
    ) {
        viewModelScope.launch {
            val meal = Meal(
                date = date.toEpochDays(),
                mealType = _selectedMealType.value,
                foodName = foodName,
                proteinGrams = protein,
                calories = calories,
                carbsGrams = carbs,
                fatGrams = fat
            )
            repository.logMeal(meal)
        }
    }
}

class RecipesViewModel(private val repository: Repository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    private val _selectedCategory = MutableStateFlow<RecipeCategory?>(null)
    val selectedCategory: StateFlow<RecipeCategory?> = _selectedCategory
    
    private val _showQuickOnly = MutableStateFlow(false)
    val showQuickOnly: StateFlow<Boolean> = _showQuickOnly
    
    private val _showMealPrepOnly = MutableStateFlow(false)
    val showMealPrepOnly: StateFlow<Boolean> = _showMealPrepOnly
    
    val allRecipes = repository.allRecipes
    
    val filteredRecipes = combine(allRecipes, _searchQuery, _selectedCategory, _showQuickOnly, _showMealPrepOnly) { recipes, query, category, quickOnly, mealPrepOnly ->
        recipes.filter { recipe ->
            (query.isBlank() || recipe.name.contains(query, ignoreCase = true)) &&
            (category == null || recipe.category == category) &&
            (!quickOnly || recipe.isQuick) &&
            (!mealPrepOnly || recipe.isMealPrep)
        }
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setCategory(category: RecipeCategory?) {
        _selectedCategory.value = category
    }
    
    fun toggleQuickFilter() {
        _showQuickOnly.value = !_showQuickOnly.value
    }
    
    fun toggleMealPrepFilter() {
        _showMealPrepOnly.value = !_showMealPrepOnly.value
    }
    
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _showQuickOnly.value = false
        _showMealPrepOnly.value = false
    }
}

class ProgressViewModel(private val repository: Repository) : ViewModel() {
    private val _selectedPeriod = MutableStateFlow(ProgressPeriod.WEEKLY)
    val selectedPeriod: StateFlow<ProgressPeriod> = _selectedPeriod
    
    private val _weekStart = MutableStateFlow(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.ordinal))
    val weekStart: StateFlow<LocalDate> = _weekStart
    
    private val _month = MutableStateFlow(LocalDate.now().month)
    private val _year = MutableStateFlow(LocalDate.now().year)
    val month: StateFlow<Int> = _month
    val year: StateFlow<Int> = _year
    
    val weeklyStats = repository.getWeeklyStats(_weekStart.value)
    val monthlyStats = repository.getMonthlyStats(_year.value, _month.value.ordinal + 1)
    
    fun setPeriod(period: ProgressPeriod) {
        _selectedPeriod.value = period
    }
    
    fun setWeekStart(date: LocalDate) {
        _weekStart.value = date
    }
    
    fun setMonth(month: Int, year: Int) {
        _month.value = month
        _year.value = year
    }
    
    fun previousWeek() {
        _weekStart.value = _weekStart.value.minusWeeks(1)
    }
    
    fun nextWeek() {
        _weekStart.value = _weekStart.value.plusWeeks(1)
    }
    
    fun previousMonth() {
        if (_month.value.ordinal == 0) {
            _month.value = 11 // December
            _year.value = _year.value - 1
        } else {
            _month.value = _month.value.ordinal - 1
        }
    }
    
    fun nextMonth() {
        if (_month.value.ordinal == 11) {
            _month.value = 0 // January
            _year.value = _year.value + 1
        } else {
            _month.value = _month.value.ordinal + 1
        }
    }
}

enum class ProgressPeriod {
    DAILY, WEEKLY, MONTHLY
}

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    val settings = repository.settingsFlow
    
    fun updateTargetWeight(weight: Double) {
        viewModelScope.launch {
            repository.updateTargetWeight(weight)
        }
    }
    
    fun updateCurrentWeight(weight: Double) {
        viewModelScope.launch {
            repository.updateCurrentWeight(weight)
        }
    }
    
    fun updateProteinPerKg(proteinPerKg: Double) {
        viewModelScope.launch {
            repository.updateProteinPerKg(proteinPerKg)
        }
    }
    
    fun toggleReminders(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleReminders(enabled)
        }
    }
    
    fun updateReminderTimes(times: List<String>) {
        viewModelScope.launch {
            repository.updateReminderTimes(times)
        }
    }
    
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleNotifications(enabled)
        }
    }
    
    fun updateThemeMode(mode: Int) {
        viewModelScope.launch {
            repository.updateThemeMode(mode)
        }
    }
}