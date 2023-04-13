package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.SingleMealCardBinding
import com.example.easyfood.pojo.MealByCategory

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavouriteMealsViewHolder>() {
    private var mealsList = listOf<MealByCategory>()
    lateinit var onMealClick: ((MealByCategory) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<MealByCategory>() {
        override fun areItemsTheSame(oldItem: MealByCategory, newItem: MealByCategory): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealByCategory, newItem: MealByCategory): Boolean {
            return oldItem == newItem
        }


    }
    val differ = AsyncListDiffer(this, diffUtil)

    fun setMealList(meals: List<MealByCategory>) {
        mealsList = meals
        notifyDataSetChanged()
    }

    fun getMealByPosition(position: Int): MealByCategory {
        return mealsList[position]
    }

    class FavouriteMealsViewHolder(val binding: SingleMealCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {

        return FavouriteMealsViewHolder(
            SingleMealCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mealsList.size

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onMealClick.invoke(mealsList[position])
        }
    }
}