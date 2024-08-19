package com.example.billbill_template.post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostBinding
import com.example.billbill_template.ui.home.HomeCategoryRVAdapter.Companion.categoryList
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.message.ChattingActivity
import com.example.billbill_template.ui.message.CreateChattingRequest
import com.example.billbill_template.ui.message.CreateChattingResponse
import com.example.billbill_template.ui.message.GetChattingByIdData
import retrofit2.Call
import retrofit2.Response

class PostFragment : Fragment(){

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!


    private var thisId = 0

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
        thisId = postId?:0
        RetrofitClient.instance.getPostById(postId?: 0).enqueue(object : retrofit2.Callback<GetPostByIdResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetPostByIdResponse>,
                response: Response<GetPostByIdResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("PostFragment", "Response: ${response.body()}")

                    val postDetail = response.body()?.data
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
                    val now = System.currentTimeMillis() / 10000000
                    val created = postDetail?.createAt!! / 10000000
                    val minute = (now - created) / 60
                    if (minute / 60 <= 0) {
                        text = "${minute}분 전"
                    } else if (minute / 60 > 0 && minute / 1440 <= 0) {
                        text = "${minute/60}시간 전"
                    } else {
                        text = "${minute/1440}일 전"
                    }
//                    // Unix 타임스탬프를 Instant로 변환
//                    val instant = Instant.ofEpochSecond(1723799035 / 1000)
//
//                    // Instant를 시스템의 기본 시간대에 맞춰 변환
//                    val dateTime = instant.atZone(ZoneId.systemDefault())
//
//                    // DateTimeFormatter를 사용해 "yyyy년 MM월 dd일 HH시 mm분" 형식으로 변환
//                    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분")
//                    val formattedDateTime = dateTime.format(formatter)
//
//                    println("변환된 시간: $formattedDateTime")
//                    val text = timeAgo(postDetail?.createAt!!)
                    binding.postDetailCategoryTv.text = "${category} · ${text}"

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
//                        val gson = Gson()
//                        val chattingJson = gson.toJson(oldVersionChatting)
//                        putString("chatting", chattingJson)
                    }
                }).commitAllowingStateLoss()
        }

        //'빌리기' 버튼
        binding.postButtonTv.setOnClickListener {
            createChatting(thisId)
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



    inner class PopupListener() : PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.post_popup_edit -> requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PostEditFragment().apply {
                        arguments = Bundle().apply {
                            putInt("postId", thisId)
                        }
                    })
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
//                R.id.post_popup_isLent ->
//                R.id.post_popup_delete ->
            }
            return false
        }

    }

    private fun createChatting(postId: Int) {
        val createChattingRequest = CreateChattingRequest(postId)
        RetrofitClient.instance.createChatting(createChattingRequest).enqueue(object : retrofit2.Callback<CreateChattingResponse> {
            override fun onResponse(
                call: Call<CreateChattingResponse>,
                response: Response<CreateChattingResponse>
            ) {
                Log.d("PostFragment", "Response: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val chatId = response.body()!!.data?.chatId
                    if (chatId != null) {
                        changeChattingActivity(chatId)
                    }
                } else {
                    Log.e("PostFragment", "채팅방 생성 실패 - 오류: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "채팅방을 생성하지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CreateChattingResponse>, t: Throwable) {
                Log.e("PostFragment", "createChatting onFailure: ${t.localizedMessage}")
                t.printStackTrace()
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changeChattingActivity(chatId: Int) {
        Log.d("PostFragment", "chatId - $chatId")
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        intent.putExtra("chatId", chatId)
        startActivity(intent)
    }
//    fun timeAgo(createAt: Long): String {
//        val now = System.currentTimeMillis() / 1000 // 현재 시간을 초 단위 Unix 타임스탬프로 변환
//        val diffInSeconds = now - createAt
//
//        return when {
//            diffInSeconds < 60 -> "$diffInSeconds 초 전"
//            diffInSeconds < TimeUnit.HOURS.toSeconds(1) -> "${diffInSeconds / 60} 분 전"
//            diffInSeconds < TimeUnit.DAYS.toSeconds(1) -> "${diffInSeconds / 3600} 시간 전"
//            else -> "${diffInSeconds / TimeUnit.DAYS.toSeconds(1)} 일 전"
//        }
//    }
}