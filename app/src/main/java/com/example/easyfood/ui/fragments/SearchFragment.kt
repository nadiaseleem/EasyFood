package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private fun searchMeals() {
        val searchQuery = binding.edSearch.text.toString()
        viewModel.searchMeal(searchQuery)

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

        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }
        // onTextEditFinished()
        onMealClicked()
        observeStatusMessageLiveData()


    }

    private fun observeStatusMessageLiveData() {
        viewModel.observeStatusMessageLiveData().observe(viewLifecycleOwner) {

            Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()


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

    /*  private fun onTextEditFinished() {
          binding.edSearch.addTextChangedListener(object : TextWatcher {

              override fun afterTextChanged(s: Editable) {
                  viewModel.searchMeal(s.toString())
              }


              override fun beforeTextChanged(s: CharSequence, start: Int,
                                             count: Int, after: Int) {
              }

              override fun onTextChanged(s: CharSequence, start: Int,
                                         before: Int, count: Int) {
              }
          })
      }
  */
    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner) { mealList ->

            var list = listOf<MealByCategory>()
            if (mealList != null) {

                list =
                    mealList.meals.map {
                        MealByCategory(
                            it.idMeal,
                            it.strMeal,
                            it.strMealThumb
                        )
                    }
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