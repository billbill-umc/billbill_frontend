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
            holder.userName.text = result.chattings?.get(position)?.user?.id.toString()

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
                val now = System.currentTimeMillis() / 1000
                val last = result.chattings?.get(position)?.lastMessage?.createAt!!
                val minute = now - last / 60
                if (minute / 60 <= 0) {
                    holder.lastTime.text = "${minute}분 전"
                } else if (minute / 60 > 0 && minute / 1440 <= 0) {
                    holder.lastTime.text = "${minute/60}시간 전"
                } else {
                    holder.lastTime.text = "${minute/1440}일 전"
                }
            }

            holder.itemView.setOnClickListener{_itemClickListener.onItemClick(result.chattings!![position].id)}
        }
    }
}