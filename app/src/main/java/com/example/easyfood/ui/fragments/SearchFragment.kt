package com.example.easyfood.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.ui.adapters.MealsAdapter
import com.example.easyfood.viewModel.HomeViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchedMealsAdapter: MealsAdapter
    val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchedMealsAdapter = MealsAdapter()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()

        observeSearchedMealsLiveData()
        binding.llNotFound.isVisible = false
        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        binding.edSearch.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMeals()
                getView()?.hideKeyboard()
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
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun observeStatusMessageLiveData() {
        viewModel.observeStatusMessageLiveData().observe(viewLifecycleOwner) {
            binding.llNotFound.isVisible = true
            binding.tvNotFound.text =
                "Sorry, we couldn’t find\n any matches for ${binding.edSearch.text}"
            searchedMealsAdapter.setMealList(listOf())

        }
    }

    private fun onMealClicked() {
        searchedMealsAdapter.onMealClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner) { mealList ->

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
        binding.rvSeachedMeals.apply {
            adapter = searchedMealsAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        }
    }


}