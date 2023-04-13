package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.retrofit.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealApi: MealApi,
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var mealLiveData = MutableLiveData<Meal>()
    private val statusMessage = MutableLiveData<String>()
    private val isFavouriteLiveData = MutableLiveData<Boolean>()

    private val comspositeDisposable = CompositeDisposable()
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
                statusMessage.value = it.localizedMessage.toString()
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

    fun observeMealLiveData(): LiveData<Meal> = mealLiveData

    fun observeStatusMessageLiveData(): LiveData<String> = statusMessage
    fun observeisFavouriteLiveData(): LiveData<Boolean> = isFavouriteLiveData


    fun insertMealToFavourites(meal: MealByCategory) {

        val completable = mealDatabase.mealDao().upsertMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = completable.subscribeBy(onComplete = {
            Log.i("Inserted", "meal insrted to favourites")
        }, onError = {
            Log.i("ERROR", it.localizedMessage.toString())

        })
        comspositeDisposable.add(disposable)

    }

    fun checkMealIsFavorite(id: String) {
        val single = mealDatabase.mealDao().exists(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = single.subscribeBy(onSuccess = {
            isFavouriteLiveData.value = it
        }, onError = {
            Log.i("ERROR", it.message.toString())
        })
        comspositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        comspositeDisposable.clear()
    }

}