package com.example.billbill_template.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostBinding
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.message.Chatting
import com.example.billbill_template.ui.message.ChattingActivity
import com.google.gson.Gson
import retrofit2.http.POST

class PostFragment : Fragment(){

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val chatting = Chatting("사과", R.drawable.img_test_message_apple, "마지막 메시지1", "3분 전")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        val photoAdpater = PostPhotoVPAdapter(this)
        photoAdpater.addFragment(PostPhotoFragment(R.drawable.img_test_post_photo))
        photoAdpater.addFragment(PostPhotoFragment(R.drawable.img_test_post_tent))
        binding.postPhotoVp.adapter = photoAdpater
        binding.postPhotoVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //좌우 스크롤


        //Indicator
        binding.postPhotoIndicator.setViewPager(binding.postPhotoVp)


//        binding.postTitleTv.setText(POST.)
//        val title:String? = intent.getStringExtra("")


        binding.postBackIv.setOnClickListener {
            // 홈으로 돌아갈 때 BottomNavigationView 보이기
            (activity as? MainActivity)?.showBottomNavigation()

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()).commitAllowingStateLoss()
        }
        binding.postMoreIv.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.post_popup_menu, popup.menu)
            popup.show()

            var listener = PopupListener()
            popup.setOnMenuItemClickListener(listener)
        }

        //연결 수정 필요
        binding.postDetailUserTv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostUserHistoryFragment().apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val chattingJson = gson.toJson(chatting)
                        putString("chatting", chattingJson)
                    }
                }).commitAllowingStateLoss()
        }

        //'빌리기' 버튼
        binding.postButtonTv.setOnClickListener {
            changeMessageFragment(chatting)
        }

        //좋아요
        binding.postLikeOffIv.setOnClickListener{
            binding.postLikeOnIv.visibility = View.VISIBLE
            binding.postLikeOffIv.visibility = View.GONE
        }
        binding.postLikeOnIv.setOnClickListener{
            binding.postLikeOnIv.visibility = View.GONE
            binding.postLikeOffIv.visibility = View.VISIBLE
        }

        return root
    }



    inner class PopupListener : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.post_popup_edit -> requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PostEditFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
//                R.id.post_popup_isLent ->
//                R.id.post_popup_delete ->
            }
            return false
        }

    }

    private fun changeMessageFragment(chatting: Chatting) {
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        startActivity(intent)
    }
}