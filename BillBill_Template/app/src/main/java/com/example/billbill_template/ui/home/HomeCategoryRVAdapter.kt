package com.example.billbill_template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.R
import com.example.billbill_template.databinding.ItemHomeCategoryBinding
import com.example.billbill_template.post.GetCategory
import com.example.billbill_template.post.GetCategoryManifestResponse

class HomeCategoryRVAdapter(val result: GetCategoryManifestResponse) :
    RecyclerView.Adapter<HomeCategoryRVAdapter.ViewHolder>() {

    private var selectedPosition = 0

    companion object {
        var categoryList: MutableList<String> = mutableListOf()
    }

    interface HomeCategoryItemClickListener {
        fun onItemClick(name: String)
    }

    private lateinit var itemClickListener: HomeCategoryItemClickListener

    fun setHomeCategoryClickListener(itemClickListener: HomeCategoryItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val name : TextView = binding.homeCategoryItemTv

        fun bind(category: GetCategory, isSelected: Boolean) {
            if (isSelected) {
                binding.homeCategoryCardView.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.black)
                )
                binding.homeCategoryItemTv.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.holo_orange_light)
                )
                binding.homeCategoryCardView.setBackgroundResource(R.drawable.selected_background) // 선택된 배경
            } else {
                binding.homeCategoryCardView.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.transparent)
                )
                binding.homeCategoryItemTv.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.black)
                )
                binding.homeCategoryCardView.setBackgroundResource(R.drawable.default_background) // 기본 배경
            }

            binding.root.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
                itemClickListener.onItemClick(result.categories[position].name)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryRVAdapter.ViewHolder {
        val binding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 14 //현재 14개. '전체' 추가시 +1

    override fun onBindViewHolder(holder: HomeCategoryRVAdapter.ViewHolder, position: Int) {
        holder.bind(result.categories[position], position == selectedPosition)
        if (result.categories[position].id < 1000) {
            if (!categoryList.contains(result.categories[position].name)) {
                categoryList.add(result.categories[position].name)
            }
            holder.name.text = result.categories[position].name
        }
    }
}
