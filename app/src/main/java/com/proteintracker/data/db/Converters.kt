package com.proteintracker.data.db

import androidx.room.TypeConverter
import com.proteintracker.data.model.MealType
import com.proteintracker.data.model.RecipeCategory
import kotlinx.datetime.LocalDate
import kotlinx.datetime.formatDate
import kotlinx.datetime.parse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDays(it) }
    }

    @TypeConverter
    fun toLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDays()
    }

    @TypeConverter
    fun fromMealType(value: Int): MealType {
        return MealType.values().firstOrNull { it.order == value } ?: MealType.BREAKFAST
    }

    @TypeConverter
    fun toMealType(type: MealType): Int {
        return type.order
    }

    @TypeConverter
    fun fromRecipeCategory(value: String): RecipeCategory {
        return RecipeCategory.values().firstOrNull { it.name == value } ?: RecipeCategory.SAVORY
    }

    @TypeConverter
    fun toRecipeCategory(category: RecipeCategory): String {
        return category.name
    }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split("|").toList()
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString("|")
    }

    @TypeConverter
    fun fromJson<T>(value: String): T {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun toJson(value: Any): String {
        return json.encodeToString(value)
    }
}