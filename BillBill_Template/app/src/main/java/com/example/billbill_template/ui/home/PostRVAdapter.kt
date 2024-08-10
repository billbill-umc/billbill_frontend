package com.example.billbill_template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemHomePostBinding

class PostRVAdapter (private val posts : ArrayList<Post>) : RecyclerView.Adapter<PostRVAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemHomePostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post : Post) {
            binding.homePostUserTv.text = post.author
            binding.homePostTitleTv.text = post.itemName
            binding.homePostCostTv.text = post.deposit.toString() + "원"
            binding.homePostPhotoIv.setImageResource(post.postImage!!)
            binding.homePostUserIv.setImageResource(post.authorPhoto!!)
            binding.homePostConditionTv.text = when (post.itemCondition) {
                    0 -> "하"
                    1 -> "중"
                    2 -> "상"
                    3 -> "최상"
                else -> { "?" }
            }
        }
    }

    interface PostItemClickListener {
        fun onItemClick(post: Post)
    }
//
//    fun addItem(post: Post) {
//        posts.add(post)
//        notifyDataSetChanged()
//    }

    private lateinit var _itemClickListener: PostItemClickListener

    fun setPostItemClickListener(itemClickListener: PostItemClickListener) {
        _itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostRVAdapter.ViewHolder {
        val binding: ItemHomePostBinding = ItemHomePostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
        holder.itemView.setOnClickListener{_itemClickListener.onItemClick(posts[position])}
    }
}