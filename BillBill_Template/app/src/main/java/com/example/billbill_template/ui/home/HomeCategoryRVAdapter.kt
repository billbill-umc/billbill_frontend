package com.example.billbill_template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.R
import com.example.billbill_template.databinding.ItemHomeCategoryBinding

class HomeCategoryRVAdapter(private val categoryList: ArrayList<String>) :
    RecyclerView.Adapter<HomeCategoryRVAdapter.ViewHolder>() {

    private var selectedPosition = 0

    interface HomeCategoryItemClickListener {
        fun onItemClick(name: String)
    }

    private lateinit var itemClickListener: HomeCategoryItemClickListener

    fun setHomeCategoryClickListener(itemClickListener: HomeCategoryItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String, isSelected: Boolean) {
            binding.homeCategoryItemTv.text = category
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
                // 선택된 항목이 다시 클릭되면 선택 취소
                if (selectedPosition == adapterPosition) {
                    selectedPosition = -1
                    notifyItemChanged(adapterPosition)
                } else {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
                itemClickListener.onItemClick(category)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = categoryList.size
}
