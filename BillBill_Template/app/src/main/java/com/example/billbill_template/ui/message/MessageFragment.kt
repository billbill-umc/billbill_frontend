package com.example.billbill_template.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentMessageBinding
import com.example.billbill_template.ui.home.HomeFragment

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val chattings = ArrayList<Chatting>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messageViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)


        chattings.apply {
            add(Chatting("파인애플", R.drawable.img_test_message_pineapple, "마지막 메시지1", "5분 전"))
            add(Chatting("사과", R.drawable.img_test_message_apple, "마지막 메시지2", "3시간 전"))
            add(Chatting("오렌지", R.drawable.img_test_message_orange, "마지막 메시지3", "2일 전"))
        }

        val messageRVAdapter = MessageRVAdapter(chattings)
        binding.messageListRv.adapter = messageRVAdapter
        binding.messageListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        messageRVAdapter.setMessageItemClickListener(object : MessageRVAdapter.MessageItemClickListener{
            override fun onItemClick(chatting: Chatting) {
                changeMessageFragment(chatting)
            }
        })

        binding.messageBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()).commitAllowingStateLoss()
        }

//        val textView: TextView = binding.textNotifications
//        messageViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeMessageFragment(chatting: Chatting) {
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        startActivity(intent)
    }
}