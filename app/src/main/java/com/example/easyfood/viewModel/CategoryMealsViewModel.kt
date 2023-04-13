package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.retrofit.MealApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(
    private val mealApi: MealApi
) : ViewModel() {
    private val mealsInCateoryLiveData = MutableLiveData<MealByCategoryList>()
    private val compositeDisposable = CompositeDisposable()
    private val statusMessage = MutableLiveData<String>()

    fun getCategoryMeals(category: String) {
        val single = mealApi.getMealsInCategory(c = category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val disposable = single.subscribeBy(onSuccess = { meals ->
            if (meals != null)
                mealsInCateoryLiveData.value = meals
        }, onError = {
            statusMessage.value = it.localizedMessage.toString()
            Log.i("ERROR", it.localizedMessage.toString())
        })

        compositeDisposable.add(disposable)

    }


    fun observeMealsInCateoryLiveData(): MutableLiveData<MealByCategoryList> =
        mealsInCateoryLiveData

    fun observeStatusMessageLiveData(): LiveData<String> = statusMessage
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}