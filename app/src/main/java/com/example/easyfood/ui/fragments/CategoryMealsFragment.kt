package com.example.easyfood.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.databinding.FragmentCategoryMealsBinding
import com.example.easyfood.ui.adapters.MealsInCategoryAdapter
import com.example.easyfood.viewModel.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealsFragment : Fragment() {
    lateinit var binding: FragmentCategoryMealsBinding
    lateinit var categoryId: String
    lateinit var categoryName: String
    lateinit var categoryThumb: String
    lateinit var mealsInCategoryAdapter: MealsInCategoryAdapter
    val viewModel: CategoryMealsViewModel by activityViewModels()

    companion object {
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_THUMB = "categoryThumb"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryId = it.getString(CATEGORY_ID).toString()
            categoryName = it.getString(CATEGORY_NAME).toString()
            categoryThumb = it.getString(CATEGORY_THUMB).toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "${categoryName} Meals"
        loadingCase()
        viewModel.getCategoryMeals(categoryName)
        observeMealsInCategory()
        mealsInCategoryAdapter = MealsInCategoryAdapter()
        prepareMealsInCategoryRecycleView()
        onMealClick()
        observeStatusMessageLiveData()

    }

    private fun onMealClick() {
        mealsInCategoryAdapter.onMealClick = { meal ->
            val action = CategoryMealsFragmentDirections.actionCategoryMealsFragmentToMealFragment(
                mealId = meal.idMeal,
                mealName = meal.strMeal.toString(),
                mealThumb = meal.strMealThumb.toString()
            )

            findNavController().navigate(action)

        }

    }

    private fun prepareMealsInCategoryRecycleView() {

        binding.rvMeals.adapter = mealsInCategoryAdapter
        binding.rvMeals.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun observeMealsInCategory() {
        viewModel.observeMealsInCateoryLiveData().observe(viewLifecycleOwner) { meals ->
            onResponseCase()
            mealsInCategoryAdapter.setMealList(meals.meals)
            binding.tvCategoryCount.text =
                "Total meals in " + categoryName + ": " + meals.meals.size + " recipe(s)."
        }
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
        viewModel.observeStatusMessageLiveData().observe(viewLifecycleOwner) {

            Toast.makeText(
                context,
                "Please Check Your Internet Connection and Try Again!",
                Toast.LENGTH_LONG
            ).show()
            binding.progressIndicator.isIndeterminate = false

        }
    }
}