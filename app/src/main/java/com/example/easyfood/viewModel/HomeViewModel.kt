package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealApi: MealApi,
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private val randomMealLiveData = MutableLiveData<Meal>()
    private val popularMealsLiveData = MutableLiveData<MealByCategoryList>()
    private val categoriesLiveData = MutableLiveData<CategoryList>()
    private var favouriteMealListLiveData = MutableLiveData<List<MealByCategory>>()
    private var searchMealLiveData = MutableLiveData<MealList>()
    private val statusMessage = MutableLiveData<String>()

    private val comspositeDisposable = CompositeDisposable()

    private var saveStatus: Meal? = null

    fun getRandomMeal() {
        if (saveStatus != null) {
            randomMealLiveData.value = saveStatus!!
            return
        }
        val single = mealApi.getRandomMeal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = single.subscribeBy(
            onSuccess = { mealList ->
                if (mealList != null) {

                    randomMealLiveData.value = mealList.meals[0]
                    saveStatus = mealList.meals[0]
                }


            }, onError = {
                Log.i("ERROR", it.localizedMessage.toString())
            })

        comspositeDisposable.add(disposable)
    }

    fun observeRandomMeal(): MutableLiveData<Meal> = randomMealLiveData

    fun getPopularMeals() {
        val single = mealApi.getPopularMeals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = single.subscribeBy(onSuccess = { meals ->
            if (meals != null)
                popularMealsLiveData.value = meals
        }, onError = {
            Log.i("ERROR", it.localizedMessage.toString())
        })

        comspositeDisposable.add(disposable)

    }

    fun observePopularMealLiveData(): MutableLiveData<MealByCategoryList> = popularMealsLiveData

    fun getCategories() {
        val single = mealApi.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        val disposable = single.subscribeBy(
            onSuccess = { categories ->
                categoriesLiveData.value = categories
            }, onError = {
                Log.i("ERROR", it.message.toString())
            }

        )

        comspositeDisposable.add(disposable)

    }

    fun observeCategoriesLiveData(): MutableLiveData<CategoryList> = categoriesLiveData

    fun getFavouriteMeals() {
        val observable = mealDatabase.mealDao().getAllMeals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = observable.subscribeBy(onNext = {
            favouriteMealListLiveData.value = it
        }, onError = {
            Log.i("ERROR", it.message.toString())
        })

        comspositeDisposable.add(disposable)


    }

    fun deleteMeal(meal: MealByCategory) {
        val completable = mealDatabase.mealDao().delete(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = completable.subscribeBy(onError = {
            Log.i("ERROR", it.message.toString())
        }, onComplete = {
            Log.i("Deleted", "Meal deleted successfully!")
        })
        comspositeDisposable.add(disposable)
    }

    fun insertMealToFavourites(meal: MealByCategory) {

        val completable = mealDatabase.mealDao().upsertMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = completable.subscribeBy(onComplete = {
            Log.i("Inserted", "meal insrted to favourites")
        }, onError = {
            Log.i("ERROR", it.message.toString())

        })
    }

    fun observeFavouriteLiveData(): MutableLiveData<List<MealByCategory>> =
        favouriteMealListLiveData

    private var mealLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id: String) {
        val single = mealApi.getFullMealDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = single.subscribeBy(
            onSuccess = { mealList ->
                if (mealList != null)
                    mealLiveData.value = mealList.meals[0]
            },
            onError = {
                Log.i("Error", it.message.toString())
            })

        comspositeDisposable.add(disposable)

    }

    fun observeMealLiveData(): LiveData<Meal> = mealLiveData

    fun searchMeal(searchQuery: String) {
        val single = mealApi.searchMeal(searchQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


        val disposable = single.subscribeBy(onSuccess = { mealList ->

            if (mealList.meals != null) {
                searchMealLiveData.postValue(mealList)
            } else {
                statusMessage.postValue("Couldn't find meals,\n Try something else!")

            }
        }, onError = {
            statusMessage.postValue(it.message.toString())
            Log.i("ERROR", "meal not found")
        })
        comspositeDisposable.add(disposable)

    }

    fun observeSearchMealLiveData(): LiveData<MealList> = searchMealLiveData
    fun observeStatusMessageLiveData(): LiveData<String> = statusMessage

    override fun onCleared() {
        super.onCleared()
        comspositeDisposable.clear()
    }


}