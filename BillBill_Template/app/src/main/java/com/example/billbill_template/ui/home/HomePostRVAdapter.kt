package com.example.billbill_template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemHomePostBinding
import com.example.billbill_template.post.OldVersionPost

class HomePostRVAdapter (private val oldVersionPosts : ArrayList<OldVersionPost>) : RecyclerView.Adapter<HomePostRVAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemHomePostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(oldVersionPost : OldVersionPost) {
            binding.homePostUserTv.text = oldVersionPost.author
            binding.homePostTitleTv.text = oldVersionPost.itemName
            binding.homePostCostTv.text = "${oldVersionPost.price}원"
            binding.homePostPhotoIv.setImageResource(oldVersionPost.postImage!!)
            binding.homePostUserIv.setImageResource(oldVersionPost.authorPhoto!!)
            binding.homePostConditionTv.text = when (oldVersionPost.itemCondition) {
                0 -> "상태: 하"
                1 -> "상태: 중"
                2 -> "상태: 상"
                3 -> "상태: 최상"
                else -> { "?" }
            }
        }
    }

    interface PostItemClickListener {
        fun onItemClick(oldVersionPost: OldVersionPost)
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHomePostBinding = ItemHomePostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = oldVersionPosts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldVersionPosts[position])
        holder.itemView.setOnClickListener{_itemClickListener.onItemClick(oldVersionPosts[position])}
    }
}