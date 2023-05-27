package com.example.easyfood.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.FragmentMealBinding
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.viewModel.MealViewModel

const val IS_FAVOURITE = "IsFavourite"

class MealFragment : Fragment() {

    private lateinit var binding: FragmentMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private var meal: MealByCategory? = null
    private var isFavourite: Boolean = true
    val viewModel: MealViewModel by activityViewModels()

    companion object {
        const val MEAL_ID = "mealId"
        const val MEAL_NAME = "mealName"
        const val MEAL_THUMB = "mealThumb"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID).toString()
            mealName = it.getString(MEAL_NAME).toString()
            mealThumb = it.getString(MEAL_THUMB).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val savedBool = savedInstanceState?.getBoolean(IS_FAVOURITE)
        savedBool?.let {
            isFavourite = it
        }
        setInformationOnViews()

        loadingCase()

        this.viewModel.getMealDetails(mealId)
        observeMealDetails()

        onYoutubeImageClick()

        onFavouriteButtonClick()

        observesIsFavouriteLiveData()

        observeStatusMessageLiveData()
    }

    private fun observesIsFavouriteLiveData() {

        this.viewModel.observeisFavouriteLiveData().observe(viewLifecycleOwner) {
            isFavourite = it

            if (isFavourite) {
                binding.btnAddToFavourites.setImageResource(R.drawable.ic_is_favourite)
            } else {
                binding.btnAddToFavourites.setImageResource(R.drawable.ic_favourites)
            }
        }


    }

    private fun observeStatusMessageLiveData() {
        this.viewModel.observeStatusMessageLiveData().observe(viewLifecycleOwner) {

            Toast.makeText(
                context,
                "Please Check Your Internet Connection and Try Again!",
                Toast.LENGTH_LONG
            ).show()
            binding.progressBar.isIndeterminate = false


        }
    }

    private fun onFavouriteButtonClick() {


        binding.btnAddToFavourites.setOnClickListener {

            if (isFavourite) {
                this.viewModel.deleteMeal(meal!!)

                binding.btnAddToFavourites.setImageResource(R.drawable.ic_favourites)
                isFavourite = !isFavourite

            } else {
                this.viewModel.insertMealToFavourites(meal!!)

                binding.btnAddToFavourites.setImageResource(R.drawable.ic_is_favourite)
                isFavourite = !isFavourite

            }
        }


    }


    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))

            startActivity(intent)


        }
    }

    private fun setInformationOnViews() {
        binding.collapsingToolbar.title = mealName
        Glide.with(requireContext())
            .load(mealThumb)
            .into(binding.imgMealDetail)


    }

    private fun observeMealDetails() {

        this.viewModel.observeMealLiveData().observe(viewLifecycleOwner) { meal ->
            onResponseCase()
            binding.tvContent.text = meal.strInstructions
            binding.tvCategoryInfo.text = "Category: " + meal.strCategory
            binding.tvAreaInfo.text = "Area: " + meal.strArea
            this.viewModel.checkMealIsFavorite(meal.idMeal)
            youtubeLink = meal.strYoutube as String
            this.meal = MealByCategory(
                idMeal = meal.idMeal,
                strMeal = meal.strMeal,
                strMealThumb = meal.strMealThumb
            )

        }
    }


    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
        binding.btnAddToFavourites.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvContent.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE

    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.btnAddToFavourites.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvContent.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_FAVOURITE, isFavourite)
    }


}