package com.example.billbill_template.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemMessageListBinding

class MessageRVAdapter (val result: GetChattingsData) : RecyclerView.Adapter<MessageRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        val userName : TextView = binding.messageListUserTv
        val userAvatar : ImageView = binding.messageListUserIv
        val lastMessage : TextView = binding.messageListDetailTv
        val lastTime : TextView = binding.messageListTimeTv
    }

    interface MessageItemClickListener {
        fun onItemClick(id: Int)
    }

    private lateinit var _itemClickListener: MessageItemClickListener

    fun setMessageItemClickListener(itemClickListener: MessageItemClickListener) {
        _itemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MessageRVAdapter.ViewHolder {
        val binding =
            ItemMessageListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = result.chattings?.size?: 0

    override fun onBindViewHolder(holder: MessageRVAdapter.ViewHolder, position: Int) {

        if(result.chattings != null) {
            holder.userName.text = result.chattings?.get(position)?.user?.username

            //마지막 메시지가 텍스트일 때
            if(result.chattings?.get(position)?.lastMessage?.type == "MESSAGE") {
                holder.lastMessage.text = result.chattings?.get(position)?.lastMessage?.content
            } else if (result.chattings?.get(position)?.lastMessage?.type == "IMAGE") { //마지막 메시지가 사진일 때
                holder.lastMessage.text = "사진을 전송했습니다."
            }

            if(result.chattings?.get(position)?.user?.avatar != null) {
                //TODO avatar 이미지 삽입
            }

            if(result.chattings?.get(position)?.lastMessage != null) {
                val createdAt = result.chattings.get(position).lastMessage!!.createdAt
                val timeAgo = getTimeAgo(createdAt)
            }

            holder.itemView.setOnClickListener{_itemClickListener.onItemClick(result.chattings!![position].id)}
        }
    }
    fun getTimeAgo(createdAt: Long): String {
        val now = System.currentTimeMillis() / 1000 // 현재 시간을 초 단위로 변환
        val diff = now - createdAt // 현재 시간과 작성 시간의 차이 (초 단위)

        return when {
            diff < 60 -> "${diff}초 전" // 1분 미만일 때
            diff < 3600 -> "${diff / 60}분 전" // 1시간 미만일 때
            diff < 86400 -> "${diff / 3600}시간 전" // 1일 미만일 때
            diff < 2592000 -> "${diff / 86400}일 전" // 30일 미만일 때
            diff < 31536000 -> "${diff / 2592000}개월 전" // 1년 미만일 때
            else -> "${diff / 31536000}년 전" // 1년 이상일 때
        }
    }
}