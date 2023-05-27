package com.example.easyfood.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
        // onTextEditFinished()

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
                "Sorry, we could not find\n any matches for ${binding.edSearch.text}"
            searchedMealsAdapter.setMealList(listOf())

        }
    }

    private fun onMealClicked() {
        searchedMealsAdapter.onMealClick = { meal ->
            val action = SearchFragmentDirections.actionSearchFragmentToMealFragment(
                mealId = meal.idMeal,
                mealName = meal.strMeal.toString(),
                mealThumb = meal.strMealThumb.toString()
            )
            findNavController().navigate(action)
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
        binding.rvSearchedMeals.apply {
            adapter = searchedMealsAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

}