package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.bottomSheet.BottomSheetFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.ui.activities.SearchActivity
import com.example.easyfood.ui.adapters.CategoriesAdapter
import com.example.easyfood.ui.adapters.MostPopularMealAdapter
import com.example.easyfood.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var popularMealAdapter: MostPopularMealAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var randomMeal: Meal? = null
    val viewModel: HomeViewModel by activityViewModels()

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_ID = "com.example.easyfood.fragments.idCategory"
        const val CATEGORY_NAME = "com.example.easyfood.fragments.nameCategory"
        const val CATEGORY_THUMB = "com.example.easyfood.fragments.thumbCategory"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularMealAdapter = MostPopularMealAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        preparePopularMealsRecyclerView()
        viewModel.getPopularMeals()
        observePopularMeals()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategories()
        onCategroyClick()
        onLongPress()
        onserachClick()


    }

    private fun onserachClick() {
        binding.imgSearch.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
    }


    private fun onLongPress() {
        popularMealAdapter.onItemlongClick = { meal ->
            BottomSheetFragment.newInstance(meal.idMeal).show(childFragmentManager, "Meal Info")
        }
    }


    private fun onCategroyClick() {
        categoriesAdapter.onCategryClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_ID, category.idCategory)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            intent.putExtra(CATEGORY_THUMB, category.strCategoryThumb)
            startActivity(intent)
        }
    }


    fun observeRandomMeal() {

        viewModel.observeRandomMeal().observe(viewLifecycleOwner, object : Observer<Meal> {
            override fun onChanged(t: Meal?) {
                t?.let { meal ->
                    randomMeal = meal
                    Glide.with(this@HomeFragment)
                        .load(meal.strMealThumb)
                        .into(binding.imgRandomMeal)
                }
            }

        })
    }

    private fun onRandomMealClick() {
        randomMeal?.let { randomMeal ->
            binding.randomMeal.setOnClickListener {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, randomMeal.idMeal)
                intent.putExtra(MEAL_NAME, randomMeal.strMeal)
                intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

                startActivity(intent)
            }
        }
    }


    private fun preparePopularMealsRecyclerView() {
        binding.recViewMealsPopular.apply {
            adapter = popularMealAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun observePopularMeals() {
        viewModel.observePopularMealLiveData().observe(viewLifecycleOwner) { popularMealList ->
            popularMealAdapter.setMeals(popularMealList.meals)

        }

    }


    private fun onPopularItemClick() {
        popularMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }


    private fun prepareCategoriesRecyclerView() {
        binding.recyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoriesList(categories.categories)
        }
    }


}