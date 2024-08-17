package com.example.billbill_template.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.billbill_template.R
import com.example.billbill_template.databinding.ItemHomePostBinding
import com.example.billbill_template.post.GetPostsData
import com.example.billbill_template.post.GetPostsPosts

class HomePostRVAdapter(private var result: GetPostsData) : RecyclerView.Adapter<HomePostRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomePostBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.homePostTitleTv
        val price: TextView = binding.homePostCostTv
        val condition: TextView = binding.homePostConditionTv
        val thumbnails: ImageView = binding.homePostPhotoIv
        val author: TextView = binding.homePostUserTv
        val avatar: ImageView = binding.homePostUserIv
    }

    interface PostItemClickListener {
        fun onItemClick(id: Int)
    }

    private lateinit var _itemClickListener: PostItemClickListener

    fun setPostItemClickListener(itemClickListener: PostItemClickListener) {
        _itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHomePostBinding = ItemHomePostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = result.posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = result.posts[position]

        // Null 값을 안전하게 처리
        holder.title.text = post.itemName ?: "제목 없음"
        holder.price.text = "${post.price ?: "가격 미정"}원"
        holder.author.text = post.author.name ?: "작성자 없음"

        // 썸네일 이미지 로드
        if (!post.thumbnail.isNullOrEmpty()) {
            Glide.with(holder.thumbnails.context)
                .load(post.thumbnail)
                .placeholder(R.drawable.img_test_home_list_photo) // 기본 이미지 설정
                .into(holder.thumbnails)
        } else {
            holder.thumbnails.setImageResource(R.drawable.img_test_home_list_photo) // 기본 이미지 설정
        }

        // avatar 이미지 로드
        if (!post.author.avatar.isNullOrEmpty()) {
            Glide.with(holder.avatar.context)
                .load(post.author.avatar)
                .placeholder(R.drawable.img_test_home_user) // 기본 아바타 설정
                .into(holder.avatar)
        } else {
            holder.avatar.setImageResource(R.drawable.img_test_home_user) // 기본 아바타 설정
        }

        // 아이템 상태 표시
        holder.condition.text = when (post.itemCondition ?: "UNKNOWN") {
            "NEW" -> "상태: 최상"
            "HIGH" -> "상태: 상"
            "MIDDLE" -> "상태: 중"
            "LOW" -> "상태: 하"
            else -> "상태: ?"
        }

        // hashCode()를 호출하기 전에 null 확인
        val itemName = post.itemName ?: ""
        val itemNameHashCode = itemName.hashCode()

        // 필요한 경우 로그 추가
        Log.d("HomePostRVAdapter", "Item Name HashCode: $itemNameHashCode")

        holder.itemView.setOnClickListener {
            _itemClickListener.onItemClick(post.id)
        }
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newData: GetPostsData) {
        result = newData
        notifyDataSetChanged() // 전체 데이터 갱신
    }
}