package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.viewModel.MealViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val IS_FAVOURITE = "IsFavourite"

@AndroidEntryPoint
class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private var meal: MealByCategory? = null
    private var isFavourite: Boolean = true
    val viewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMealInformationFromIntent()
        setInformationOnViews()

        loadingCase()

        this.viewModel.getMealDetails(mealId)
        observeMealDetails()

        onYoutubeImageClick()

        onfavouriteButtonClick()

        observesisFavouriteLiveData()

        observestatusMessageLiveData()

    }

    private fun observesisFavouriteLiveData() {

        this.viewModel.observeisFavouriteLiveData().observe(this) {
            isFavourite = it

            if (isFavourite) {
                binding.btnAddToFavourites.setImageResource(R.drawable.ic_is_favourite)
            } else {
                binding.btnAddToFavourites.setImageResource(R.drawable.ic_favourites)
            }
        }


    }

    private fun observestatusMessageLiveData() {
        this.viewModel.observeStatusMessageLiveData().observe(this) {

            Toast.makeText(
                this,
                "Please Check Your Internet Connection and Try Again!",
                Toast.LENGTH_LONG
            ).show()
            binding.progressBar.isIndeterminate = false


        }
    }

    private fun onfavouriteButtonClick() {


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
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)


    }

    private fun observeMealDetails() {

        this.viewModel.observeMealLiveData().observe(this) { meal ->
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

    private fun getMealInformationFromIntent() {

        val intent = getIntent()
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID).toString()
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME).toString()
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB).toString()

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

    override fun onStop() {
        super.onStop()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(IS_FAVOURITE, isFavourite)


    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        val savedBool = savedInstanceState?.getBoolean(IS_FAVOURITE)
        savedBool?.let {
            isFavourite = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

}

