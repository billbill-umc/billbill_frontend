package com.example.billbill_template.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostBinding
import com.example.billbill_template.ui.home.HomeCategoryRVAdapter.Companion.categoryList
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.message.ChattingActivity
import com.example.billbill_template.ui.message.CreateChattingRequest
import com.example.billbill_template.ui.message.CreateChattingResponse
import com.example.billbill_template.ui.mypage.UserInfoResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostFragment : Fragment(){

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private var thisId = 0

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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
                    if(postDetail?.author?.avatar.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(R.drawable.img_test_home_user) // 기본 이미지 설정
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.postDetailUserIv)
                    } else {
                        Glide.with(requireContext())
                            .load(postDetail?.author?.avatar)
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.postDetailUserIv)
                    }
//                    binding.postDetailUserIv.setImageResource(postDetail?.author?.avatar?.toInt()!!)
                    
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
            val isAuthor =  false //TODO
            showPopupMenu(it, isAuthor)
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            imageUri = data.data
//            binding.mpUserUdIv.setImageURI(imageUri)
//
//            // Glide를 사용하여 이미지를 동그랗게 로드
//            imageUri?.let { uri ->
//                Glide.with(this)
//                    .load(uri)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(binding.mpUserUdIv)
//
//                // 아바타 업로드 API 호출
//                val file = getFileFromUri(uri)
//                file?.let {
//                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
//                    val body = MultipartBody.Part.createFormData("avatar", it.name, requestFile)
//
//                    // 아바타 업로드 API 호출
//                    RetrofitClient.instance.uploadAvatar(body).enqueue(object : Callback<Void> {
//                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                            if (response.isSuccessful) {
//                                Toast.makeText(context, "아바타가 업로드되었습니다.", Toast.LENGTH_SHORT).show()
//                                saveUserInfo(username = binding.mpUdInputName.text.toString(),
//                                    phoneNumber = binding.mpUdInputTp.text.toString(),
//                                    avatarUrl = uri.toString())
//                                parentFragmentManager.popBackStack()  // MyPageFragment로 돌아가기
//                            } else {
//                                Toast.makeText(context, "아바타 업로드 실패.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<Void>, t: Throwable) {
//                            Toast.makeText(context, "네트워크 오류 발생.", Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                } ?: Toast.makeText(context, "파일 경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun getFileFromUri(uri: Uri): File? {
//        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
//        cursor?.moveToFirst()
//        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
//        val filePath = columnIndex?.let { cursor.getString(it) }
//        cursor?.close()
//        return filePath?.let { File(it) }
//    }
//
//    private fun saveUserInfo(username: String, phoneNumber: String, avatarUrl: String?) {
//        val sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("USERNAME", username)
//        editor.putString("PHONENUMBER", phoneNumber)
//        editor.putString("AVATARURL", avatarUrl) // 아바타 URL도 저장
//        editor.apply()  // 비동기로 저장
//    }


    fun showPopupMenu(view: View, isAuthor: Boolean) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater: MenuInflater = popupMenu.menuInflater

        // 작성자인 경우와 아닌 경우 각각 다른 메뉴 리소스를 불러옵니다.
        if (isAuthor) {
            inflater.inflate(R.menu.post_popup_menu_author, popupMenu.menu)
        } else {
            inflater.inflate(R.menu.post_popup_menu_not_author, popupMenu.menu)
        }

        // 메뉴 항목 선택 시 처리
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.post_popup_edit -> { // 수정
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PostEditFragment().apply {
                            arguments = Bundle().apply {
                                putInt("postId", thisId)
                            }
                        })
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                    true
                }
                R.id.post_popup_isLent -> { //대여완료
                    Toast.makeText(view.context, "대여완료 선택", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.post_popup_delete -> { //삭제
                    RetrofitClient.instance.deletePost(thisId).enqueue(object : retrofit2.Callback<DeletePostResponse> {
                        override fun onResponse(
                            call: Call<DeletePostResponse>,
                            response: Response<DeletePostResponse>
                        ) {
                            Log.d("PostFragment", "Response: ${response.body()}")
                            if (response.isSuccessful && response.body() != null) {
                                Log.d("PostFragment", "게시글을 삭제했습니다.")
                            } else {
                                Log.e("PostFragment", "게시글 삭제 실패 - 오류: ${response.code()} - ${response.message()}")
                                Toast.makeText(context, "게시글을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                            Log.e("PostFragment", "delete Posts onFailure: ${t.localizedMessage}")
                            t.printStackTrace()
                            Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }

                    })
                    true
                }
                R.id.post_popup_history -> { //사용자 내역
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PostUserHistoryFragment().apply {
                            arguments = Bundle().apply {
//                        val gson = Gson()
//                        val chattingJson = gson.toJson(oldVersionChatting)
//                        putString("chatting", chattingJson)
                                //TODO 연결수정 필요
                            }
                        }).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
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