package com.example.billbill_template.ui.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentMessageBinding
import com.example.billbill_template.ui.home.HomeFragment

class MessageFragment : Fragment(), MessageView {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var getMessagesAdapter : MessageRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messageViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)

//        val messageRVAdapter = MessageRVAdapter(oldVersionChattings)
//        binding.messageListRv.adapter = messageRVAdapter
//        binding.messageListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        messageRVAdapter.setMessageItemClickListener(object : MessageRVAdapter.MessageItemClickListener{
//            override fun onItemClick(oldVersionChatting: OldVersionChatting) {
//                changeMessageFragment(oldVersionChatting)
//            }
//        })

        binding.messageBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()).commitAllowingStateLoss()
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val messageService = MessageService()
        messageService.setMessageView(this)
        messageService.getMessages(requireContext())
    }

    private fun changeChattingActivity(chattingId : Int) {
        //TODO 구현
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        startActivity(intent)
    }

    override fun onGetMessagesSuccess(result: GetChattingsData) {
        getMessagesAdapter = MessageRVAdapter(result)
        binding.messageListRv.adapter = getMessagesAdapter
        getMessagesAdapter.setMessageItemClickListener(object : MessageRVAdapter.MessageItemClickListener {
            override fun onItemClick(id: Int) {
                changeChattingActivity(id)
                Log.d("MessageFragment", "click item id : ${id}")
            }
        })
        Log.d("MessageFragment", "Get Messages Success")
    }

    override fun onGetMessagesFailure(message: String) {
        Log.d("MessageFragment", "Get Messages Failure = ${message}")
    }
}