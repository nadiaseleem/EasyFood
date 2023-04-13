package com.example.easyfood.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.FragmentBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "param1"

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private var meal: Meal? = null

    private lateinit var binding: FragmentBottomSheetBinding
    val viewModel: HomeViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
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
        viewModel.getMealDetails(mealId as String)

        observeMealDetails()
        onReadMoreClicked()
        onButtomSheeetClick()
    }

    private fun onReadMoreClicked() {
        binding.tvReadMoreBtnsheet.setOnClickListener {
            if (meal != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal!!.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal!!.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal!!.strMealThumb)
                startActivity(intent)
            }
        }

    }

    private fun onButtomSheeetClick() {
        binding.bottomSheet.setOnClickListener {
            if (meal != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal!!.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal!!.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal!!.strMealThumb)
                startActivity(intent)
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}