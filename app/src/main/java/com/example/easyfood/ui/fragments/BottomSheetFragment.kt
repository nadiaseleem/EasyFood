package com.example.easyfood.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.FragmentBottomSheetBinding
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var mealId: String
    private lateinit var meal: Meal
    private lateinit var binding: FragmentBottomSheetBinding
    val viewModel: HomeViewModel by activityViewModels()

    companion object {
        const val MEAL_ID = "mealId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID).toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMealDetails(mealId)
        observeMealDetails()
        onReadMoreClicked()
        onBottomSheetClick()
    }

    private fun onReadMoreClicked() {
        binding.tvReadMoreBtnsheet.setOnClickListener {
            meal?.let { meal ->
                val action = BottomSheetFragmentDirections.actionBottomSheetFragmentToMealFragment(
                    mealId = meal.idMeal,
                    mealName = meal.strMeal.toString(),
                    mealThumb = meal.strMealThumb.toString()
                )

                findNavController().navigate(action)

            }

        }
    }


    private fun onBottomSheetClick() {
        binding.bottomSheet.setOnClickListener {
            meal?.let { meal ->
                val action = BottomSheetFragmentDirections.actionBottomSheetFragmentToMealFragment(
                    mealId = meal.idMeal,
                    mealName = meal.strMeal.toString(),
                    mealThumb = meal.strMealThumb.toString()
                )

                findNavController().navigate(action)

            }
        }
    }

    private fun observeMealDetails() {
        viewModel.observeMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@BottomSheetFragment)
                .load(meal.strMealThumb)
                .into(binding.imgCategory)
            binding.tvMealCategory.text = meal.strCategory
            binding.tvMealCountry.text = meal.strArea
            binding.tvMealNameInBtmsheet.text = meal.strMeal
            this.meal = meal

        }
    }


}