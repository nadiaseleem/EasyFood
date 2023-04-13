package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MostPopularCardBinding
import com.example.easyfood.pojo.MealByCategory

class MostPopularMealAdapter() :
    RecyclerView.Adapter<MostPopularMealAdapter.PopularMealViewHolder>() {
    var mealList = listOf<MealByCategory>()
    lateinit var onItemClick: ((MealByCategory) -> Unit)
    lateinit var onItemlongClick: ((MealByCategory) -> Unit)

    inner class PopularMealViewHolder(val binding: MostPopularCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setMeals(meals: List<MealByCategory>) {
        mealList = meals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            MostPopularCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mealList.size

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemlongClick.invoke(mealList[position])
            true
        }
    }


}