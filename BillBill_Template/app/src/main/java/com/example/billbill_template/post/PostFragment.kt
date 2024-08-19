package com.example.billbill_template.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostBinding
import com.example.billbill_template.ui.home.HomeCategoryRVAdapter.Companion.categoryList
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.message.OldVersionChatting
import com.example.billbill_template.ui.message.ChattingActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class PostFragment : Fragment(){

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val oldVersionChatting = OldVersionChatting("사과", R.drawable.img_test_message_apple, "마지막 메시지1", "3분 전")

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






        val postId = arguments?.getInt("postId")
        RetrofitClient.instance.getPostById(postId?: 0).enqueue(object : retrofit2.Callback<GetPostByIdResponse> {
            override fun onResponse(
                call: Call<GetPostByIdResponse>,
                response: Response<GetPostByIdResponse>
            ) {
                if (response.isSuccessful) {
                    val postDetail = response.body()?.data?.post?.firstOrNull()
                    binding.postTitleTv.text = postDetail?.itemName
                    binding.postDetailTv.text = postDetail?.description
                    binding.postDetailCostTv.text = "${postDetail?.price}원"
                    binding.postDetailDepositTv.text = "보증금 ${postDetail?.deposit}원"
                    binding.postDetailUserTv.text = postDetail?.author?.username
                    binding.postDetailUserIv.setImageResource(postDetail?.author?.avatar?.toInt()!!)

                    val condition = when(postDetail?.itemCondition) {
                        "NEW" -> "최상"
                        "HIGH" -> "상"
                        "MIDDLE" -> "중"
                        "LOW" -> "하"
                        else -> "?"
                    }
                    binding.postTitleSmallTv.text = "${postDetail?.price}원 ${condition}"

                    var category = "전체"
                    for(i in 1..categoryList.size) {
                        if (i == postDetail?.category) {
                            category = categoryList[i - 1]
                        }
                    }
                    val text : String
                    val now = System.currentTimeMillis() / 1000
                    val created = postDetail?.createAt!!
                    val minute = (now - created) / 60
                    if (minute / 60 <= 0) {
                        text = "${minute}분 전"
                    } else if (minute / 60 > 0 && minute / 1440 <= 0) {
                        text = "${minute/60}시간 전"
                    } else {
                        text = "${minute/1440}일 전"
                    }
                    binding.postDetailCategoryTv.text = "${category} · ${text} / 서울특별시 강남구"

                    Log.d("PostFragment", "게시물을 성공적으로 불러왔습니다.")
                } else {
                    Log.e("PostFragment", "게시물 불러오기 실패 - 오류: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "게시물을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetPostByIdResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })





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
                        val chattingJson = gson.toJson(oldVersionChatting)
                        putString("chatting", chattingJson)
                    }
                }).commitAllowingStateLoss()
        }

        //'빌리기' 버튼
        binding.postButtonTv.setOnClickListener {
            changeMessageFragment(oldVersionChatting)
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

    private fun changeMessageFragment(oldVersionChatting: OldVersionChatting) {
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        startActivity(intent)
    }
}