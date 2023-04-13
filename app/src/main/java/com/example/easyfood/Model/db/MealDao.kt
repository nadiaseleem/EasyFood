package com.example.easyfood.db

import androidx.room.*
import com.example.easyfood.pojo.MealByCategory
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MealDao {

//insert and update "replace" if exsisting

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertMeal(meal: MealByCategory): Completable

    @Delete
    fun delete(meal: MealByCategory): Completable

    @Query("SELECT * FROM Meal_Table")
    fun getAllMeals(): Observable<List<MealByCategory>>

    @Query("SELECT EXISTS (SELECT * FROM Meal_Table WHERE idMeal= :id)")
    fun exists(id: String): Single<Boolean>


}