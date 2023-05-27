package com.example.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.pojo.Meal
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
        const val MEAL_ID = "mealId"
        const val MEAL_NAME = "mealName"
        const val MEAL_THUMB = "mealThumb"
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
        onCategoryClick()
        onLongPress()
        onsearchClick()


    }

    private fun onsearchClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }


    private fun onLongPress() {
        popularMealAdapter.onItemlongClick = { meal ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(mealId = meal.idMeal)
            findNavController().navigate(action)
        }
    }


    private fun onCategoryClick() {
        categoriesAdapter.onCategryClick = { category ->
            val action = HomeFragmentDirections.actionHomeFragmentToCategoryMealsFragment(
                categoryId = category.idCategory,
                categoryName = category.strCategory,
                categoryThumb = category.strCategoryThumb
            )
            findNavController().navigate(action)
        }
    }


    private fun observeRandomMeal() {

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

        binding.randomMeal.setOnClickListener {
            randomMeal?.let { randomMeal ->
                val action = HomeFragmentDirections.actionHomeFragmentToMealFragment(
                    mealId = randomMeal.idMeal,
                    mealName = randomMeal.strMeal.toString(),
                    mealThumb = randomMeal.strMealThumb.toString()
                )
                findNavController().navigate(action)
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
            val action = HomeFragmentDirections.actionHomeFragmentToMealFragment(
                mealId = meal.idMeal,
                mealName = meal.strMeal.toString(),
                mealThumb = meal.strMealThumb.toString()
            )
            findNavController().navigate(action)

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