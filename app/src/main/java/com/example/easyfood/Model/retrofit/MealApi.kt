package com.example.easyfood.retrofit

import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.pojo.MealList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Single<MealList>

    @GET("lookup.php")
    fun getFullMealDetails(@Query("i") i: String): Single<MealList>

    @GET("filter.php?c=Seafood")
    fun getPopularMeals(): Single<MealByCategoryList>

    @GET("categories.php")
    fun getCategories(): Single<CategoryList>

    @GET("filter.php")
    fun getMealsInCategory(@Query("c") c: String): Single<MealByCategoryList>

    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String): Single<MealList>

}