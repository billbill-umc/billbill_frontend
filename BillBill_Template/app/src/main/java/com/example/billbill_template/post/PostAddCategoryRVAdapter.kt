package com.example.billbill_template.post

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.R
import com.example.billbill_template.databinding.ItemHomeCategoryBinding

class PostAddCategoryRVAdapter(private var result: GetCategoryManifestResponse, private val postAddView: PostAddView) : RecyclerView.Adapter<PostAddCategoryRVAdapter.ViewHolder>() {

    var postAddCategorySelectedPosition = 0

    val postAddService = PostAddService()

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.homeCategoryItemTv

        init {
            postAddService.setPostAddView(postAddView)
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
                if (postAddCategorySelectedPosition != adapterPosition) { //다른 항목 클릭 시 기존 항목 선택 취소 + 새 항목 선택
                    val previousPosition = postAddCategorySelectedPosition
                    postAddCategorySelectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(postAddCategorySelectedPosition)
                }
                itemClickListener.onItemClick(category.name)
            }
        }
    }

    interface PostCategoryItemClickListener {
        fun onItemClick(name: String)
    }

    private lateinit var itemClickListener: PostCategoryItemClickListener

    fun setPostCategoryClickListener(itemClickListener: PostCategoryItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun updateData(newResult: GetCategoryManifestResponse) {
        this.result = newResult
        notifyDataSetChanged()  // 데이터 변경을 어댑터에 알림
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
        if (position < result.categories.size / 2) { //물물교환 카테고리 제외
            val category = result.categories[position]
            holder.bind(category, position == postAddCategorySelectedPosition)
            holder.name.text = category.name
        }
    }
}
