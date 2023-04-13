package com.example.easyfood.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.ui.adapters.MealsInCategoryAdapter
import com.example.easyfood.viewModel.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryId: String
    lateinit var categoryName: String
    lateinit var categoryThumb: String
    lateinit var mealsInCategoryAdapter: MealsInCategoryAdapter
    val viewModel: CategoryMealsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingCase()
        getInformatrionFromIntent()
        viewModel.getCategoryMeals(categoryName)
        observeMealsInCategory()
        mealsInCategoryAdapter = MealsInCategoryAdapter()
        prepareMealsInCategoryRecycleView()
        onMealClick()
        observeStatusMessageLiveData()

    }

    private fun onMealClick() {
        mealsInCategoryAdapter.onMealClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }

    }

    private fun prepareMealsInCategoryRecycleView() {

        binding.rvMeals.adapter = mealsInCategoryAdapter
        binding.rvMeals.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun observeMealsInCategory() {
        viewModel.observeMealsInCateoryLiveData().observe(this) { meals ->
            onResponseCase()
            mealsInCategoryAdapter.setMealList(meals.meals)
            binding.tvCategoryCount.text =
                "Total meals in " + categoryName + ": " + meals.meals.size + " recipe(s)."
        }
    }


    private fun getInformatrionFromIntent() {
        val intent = getIntent()
        categoryId = intent.getStringExtra(HomeFragment.CATEGORY_ID).toString()
        categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME).toString()
        categoryThumb = intent.getStringExtra(HomeFragment.CATEGORY_THUMB).toString()


    }

    private fun loadingCase() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE

        binding.tvCategoryCount.visibility = View.INVISIBLE
        binding.rvMeals.visibility = View.INVISIBLE

    }

    private fun onResponseCase() {
        binding.progressIndicator.visibility = View.INVISIBLE
        binding.tvLoading.visibility = View.INVISIBLE

        binding.tvCategoryCount.visibility = View.VISIBLE
        binding.rvMeals.visibility = View.VISIBLE
    }

    private fun observeStatusMessageLiveData() {
        viewModel.observeStatusMessageLiveData().observe(this) {

            Toast.makeText(
                this,
                "Please Check Your Internet Connection and Try Again!",
                Toast.LENGTH_LONG
            ).show()
            binding.progressIndicator.isIndeterminate = false

        }
    }
}