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

class HomeCategoryRVAdapter(private var result: GetCategoryManifestResponse, private val homeView: HomeView) :
    RecyclerView.Adapter<HomeCategoryRVAdapter.ViewHolder>() {

    private var selectedPosition = -1
    // -1 -> 첫 상태 = 카테고리 선택 X = 전체 선택
    // 0 -> 첫 상태 = 첫 번째 카테고리 선택

    val homeService = HomeService()

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

    fun updateData(newResult: GetCategoryManifestResponse) {
        this.result = newResult
        notifyDataSetChanged()  // 데이터 변경을 어댑터에 알림
    }

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.homeCategoryItemTv

        init {
            homeService.setHomeView(homeView)
        }

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
                if (selectedPosition == adapterPosition) { // 선택된 항목이 다시 클릭되면 선택 취소
                    selectedPosition = -1
                    notifyItemChanged(adapterPosition)
                } else { //다른 항목 클릭 시 기존 항목 선택 취소 + 새 항목 선택
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
                itemClickListener.onItemClick(category.name)
                homeService.getPosts(binding.root.context, selectedPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.categories.size/2 //물물교환 카테고리 제외
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < result.categories.size) {
            val category = result.categories[position]
            holder.bind(category, position == selectedPosition)
            if (category.id < 1000) {
                if (!categoryList.contains(category.name)) {
                    categoryList.add(category.name)
                }
                holder.name.text = category.name
            }
        }
    }
}
