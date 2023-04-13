package com.example.easyfood.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeconver() {


    @TypeConverter
    fun fromAnyToString(attribute: Any?): String {
        if (attribute == null)
            return ""
        return attribute as String
    }

    @TypeConverter
    fun fromstringToAny(attribute: String?): Any {
        if (attribute == null)
            return ""
        return attribute
    }
}