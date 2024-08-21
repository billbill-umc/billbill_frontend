package com.example.billbill_template.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemAddPostPhotoBinding

class PostAddPhotoRVAdapter(private var a: String) : RecyclerView.Adapter<PostAddPhotoRVAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemAddPostPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    interface PostAddPhotoClickListener {
        fun onItemClick(id: Int)
    }

    private lateinit var _itemClickListener: PostAddPhotoClickListener

    fun setPostAddPhotoClickListener(itemClickListener: PostAddPhotoClickListener) {
        _itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAddPostPhotoBinding = ItemAddPostPhotoBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = a.length ?: 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    fun updateData(newData: String) {
        a = newData
        notifyDataSetChanged() // 전체 데이터 갱신
    }
}