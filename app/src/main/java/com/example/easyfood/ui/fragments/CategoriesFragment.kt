package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.ui.adapters.CategoriesAdapter
import com.example.easyfood.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    lateinit var binding: FragmentCategoriesBinding
    lateinit var categoriesAdapter: CategoriesAdapter
    val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        viewModel.getCategories()
        observeCategories()
        onCategroyClick()

    }

    private fun prepareRecyclerView() {
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }


    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoriesList(categories.categories)
        }
    }

    private fun onCategroyClick() {
        categoriesAdapter.onCategryClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_ID, category.idCategory)
            intent.putExtra(HomeFragment.CATEGORY_NAME, category.strCategory)
            intent.putExtra(HomeFragment.CATEGORY_THUMB, category.strCategoryThumb)
            startActivity(intent)
        }
    }

}