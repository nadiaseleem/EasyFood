package com.example.easyfood.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meal_Table")
data class MealByCategory(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String?,
    val strMealThumb: String?
)