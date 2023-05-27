package com.example.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.databinding.FragmentFavouritesBinding
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.ui.adapters.MealsAdapter
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment() {
    lateinit var binding: FragmentFavouritesBinding
    lateinit var mealsAdapter: MealsAdapter
    val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealsAdapter = MealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavouriteMeals()
        onMealClick()
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val favoriteMeal = mealsAdapter.getMealByPosition(position)
                viewModel.deleteMeal(favoriteMeal)
                mealsAdapter.notifyItemRemoved(position);

                showDeleteSnackBar(favoriteMeal, position)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavouriteMeals)

    }

    private fun showDeleteSnackBar(favoriteMeal: MealByCategory, position: Int) {
        Snackbar.make(requireView(), "Meal was deleted", Snackbar.LENGTH_LONG).apply {
            setAction("undo", View.OnClickListener {
                viewModel.insertMealToFavourites(favoriteMeal)
                mealsAdapter.notifyItemInserted(position);

            }).show()
        }
    }


    private fun onMealClick() {
        mealsAdapter.onMealClick = { meal ->
            val action = FavouritesFragmentDirections.actionFavouritesFragmentToMealFragment(
                mealId = meal.idMeal,
                mealName = meal.strMeal.toString(),
                mealThumb = meal.strMealThumb.toString()
            )

            findNavController().navigate(action)

        }
    }

    private fun prepareRecyclerView() {
        binding.rvFavouriteMeals.apply {
            adapter = mealsAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            itemAnimator = null
        }
    }

    private fun observeFavouriteMeals() {
        viewModel.getFavouriteMeals()
        viewModel.observeFavouriteLiveData().observe(viewLifecycleOwner) {
            mealsAdapter.setMealList(it)
            mealsAdapter.differ.submitList(it)

        }
    }


}