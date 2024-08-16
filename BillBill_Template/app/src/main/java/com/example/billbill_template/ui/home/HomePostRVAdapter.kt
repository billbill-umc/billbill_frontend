package com.example.billbill_template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemHomePostBinding
import com.example.billbill_template.post.GetPostsData
import com.example.billbill_template.post.GetPostsPosts

class HomePostRVAdapter (val result: GetPostsData) : RecyclerView.Adapter<HomePostRVAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemHomePostBinding) : RecyclerView.ViewHolder(binding.root) {
        val title : TextView = binding.homePostTitleTv
        val price : TextView = binding.homePostCostTv
        val condition : TextView = binding.homePostConditionTv
        val thumbnails : ImageView = binding.homePostPhotoIv

        val author : TextView = binding.homePostUserTv
        val avatar : ImageView = binding.homePostUserIv
    }

    interface PostItemClickListener {
        fun onItemClick(id : Int)
    }

    private lateinit var _itemClickListener: PostItemClickListener

    fun setPostItemClickListener(itemClickListener: PostItemClickListener) {
        _itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePostRVAdapter.ViewHolder {
        val binding: ItemHomePostBinding = ItemHomePostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = result.posts.size

    override fun onBindViewHolder(holder: HomePostRVAdapter.ViewHolder, position: Int) {
        holder.title.text = result.posts[position].itemName
        holder.price.text = "${result.posts[position].price}원"
        holder.author.text = result.posts[position].author.name

        if(result.posts[position].thumbnail != "" && result.posts[position].thumbnail != null) {
            //미리보기 이미지 삽입
        }
        if(result.posts[position].author.avatar != "" && result.posts[position].author.avatar != null) {
            //avatar 이미지 삽입
        }
        holder.condition.text = when(result.posts[position].itemCondition) {
            "NEW" -> "상태: 최상"
            "HIGH" -> "상태: 상"
            "MIDDLE" -> "상태: 중"
            "LOW" -> "상태: 하"
            else -> "상태: ?"
        }



//        holder.bind(oldVersionPosts[position])
        holder.itemView.setOnClickListener{_itemClickListener.onItemClick(result.posts[position].id)}
    }
}