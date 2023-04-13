package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.SingleMealCardBinding
import com.example.easyfood.pojo.MealByCategory

class MealsInCategoryAdapter :
    RecyclerView.Adapter<MealsInCategoryAdapter.MealsInCategoryViewHolder>() {
    private var mealsList = listOf<MealByCategory>()
    lateinit var onMealClick: (MealByCategory) -> Unit
    fun setMealList(meals: List<MealByCategory>) {
        mealsList = meals
        notifyDataSetChanged()
    }

    inner class MealsInCategoryViewHolder(val binding: SingleMealCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsInCategoryViewHolder {
        return MealsInCategoryViewHolder(
            SingleMealCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mealsList.size

    override fun onBindViewHolder(holder: MealsInCategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onMealClick.invoke(mealsList[position])
        }
    }
}