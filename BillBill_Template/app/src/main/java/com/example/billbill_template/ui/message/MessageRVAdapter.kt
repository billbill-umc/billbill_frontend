package com.example.billbill_template.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.databinding.ItemMessageListBinding

class MessageRVAdapter (private val chattings : ArrayList<Chatting>) : RecyclerView.Adapter<MessageRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.messageListUserIv.setImageResource(chatting.avater!!)
            binding.messageListUserTv.text = chatting.user
            binding.messageListDetailTv.text = chatting.lastMessage
            binding.messageListTimeTv.text = chatting.lastTime
        }
    }

    interface MessageItemClickListener {
        fun onItemClick(chatting: Chatting)
    }

    private lateinit var _itemClickListener: MessageItemClickListener

    fun setMessageItemClickListener(itemClickListener: MessageItemClickListener) {
        _itemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageRVAdapter.ViewHolder {
        val binding: ItemMessageListBinding = ItemMessageListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = chattings.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chattings[position])
        holder.itemView.setOnClickListener{_itemClickListener.onItemClick(chattings[position])}
    }
}