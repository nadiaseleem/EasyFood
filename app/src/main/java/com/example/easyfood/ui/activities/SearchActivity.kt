package com.example.easyfood.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.ActivitySearchBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.ui.adapters.MealsAdapter
import com.example.easyfood.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchedMealsAdapter: MealsAdapter
    val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        searchedMealsAdapter = MealsAdapter()
        prepareRecyclerView()
        observeSearchedMealsLiveData()
        binding.llNotFound.isVisible = false
        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        binding.edSearch.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMeals()
                this.currentFocus?.hideKeyboard()
            }
            true
        }

        onMealClicked()
        observeStatusMessageLiveData()


    }

    private fun searchMeals() {
        val searchQuery = binding.edSearch.text.toString()
        if (searchQuery.isNotBlank()) {
            viewModel.searchMeal(searchQuery)

        } else {
            searchedMealsAdapter.setMealList(listOf())
        }
        binding.llNotFound.isVisible = false


    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun observeStatusMessageLiveData() {
        viewModel.observeStatusMessageLiveData().observe(this) {
            binding.llNotFound.isVisible = true
            binding.tvNotFound.text =
                "Sorry, we could not find\n any matches for ${binding.edSearch.text}"
            searchedMealsAdapter.setMealList(listOf())

        }
    }

    private fun onMealClicked() {
        searchedMealsAdapter.onMealClick = {
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealLiveData().observe(this) { mealList ->

            var list =
                mealList.meals.map {
                    MealByCategory(
                        it.idMeal,
                        it.strMeal,
                        it.strMealThumb
                    )
                }
            searchedMealsAdapter.setMealList(list)

        }

    }


    private fun prepareRecyclerView() {
        binding.rvSearchedMeals.apply {
            adapter = searchedMealsAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

}