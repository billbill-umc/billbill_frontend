package com.example.billbill_template.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemHomeCategoryBinding

class HomeCategoryRVAdapter(private val categorys : ArrayList<String>) : RecyclerView.Adapter<HomeCategoryRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name : String) {
            binding.homeCategoryItemTv.text = name
        }
    }

    interface HomeCategoryItemClickListener {
        fun onItemClick(name : String)
    }

    private lateinit var _itemClickListener: HomeCategoryItemClickListener

    fun setHomeCategoryClickListener(itemClickListener : HomeCategoryItemClickListener) {
        _itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeCategoryRVAdapter.ViewHolder {
        val binding : ItemHomeCategoryBinding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categorys[position])
        holder.itemView.setOnClickListener{_itemClickListener.onItemClick(categorys[position])}
    }

    override fun getItemCount(): Int = categorys.count()
}