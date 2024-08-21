package com.example.billbill_template.post

import PostAddConditionRVAdapter
import PostAddPhotoRVAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostAddBinding
import com.example.billbill_template.ui.home.HomeFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.nio.charset.StandardCharsets

class PostAddFragment : Fragment(), PostAddView {
    private var _binding: FragmentPostAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var postAddCategoryRVAdapter: PostAddCategoryRVAdapter
    private lateinit var postAddConditionRVAdapter: PostAddConditionRVAdapter

    private var conditions : List<String> = listOf("NEW", "HIGH", "MIDDLE", "LOW")
    private var conditionsKor : List<String> = listOf("최상", "상", "중", "하")

    private lateinit var photoAdapter: PostAddPhotoRVAdapter
    private val photoList: MutableList<Uri> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        // 기본 이미지 추가
        photoList.add(Uri.parse("android.resource://com.example.billbill_template/drawable/img_post_add_photo"))

        binding.postAddBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()).commitAllowingStateLoss()
        }
        binding.postAddPhotoRv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        }

        binding.postAddInputCalendarIv.setOnClickListener{}
        setupRecyclerViews()

        binding.postAddButtonTv.setOnClickListener{
            if (binding.postAddInputTitleEt.text.toString().trim().isEmpty()) {
                showToast("게시물 제목은 공란으로 둘 수 없습니다.")
            } else if (binding.postAddInputCostEt.text.toString().trim().isEmpty()) {
                showToast("상품 가격은 공란으로 둘 수 없습니다.")
            } else if (binding.postAddInputDetailEt.text.toString().trim().isEmpty()) {
                showToast("게시물 내용은 공란으로 둘 수 없습니다.")
            } else {
                val title = binding.postAddInputTitleEt.text.toString()
                val price = binding.postAddInputCostEt.text.toString().toInt()
                val description = binding.postAddInputDetailEt.text.toString()
                val depositString = binding.postAddInputDepositEt.text.toString()
                var deposit = 0
                var category = postAddCategoryRVAdapter.postAddCategorySelectedPosition + 1
                var condition = conditions[postAddConditionRVAdapter.postAddConditionSelectedPosition]

                if(depositString.isNotEmpty()) {
                    deposit = depositString.toInt()
                }

                val token = getToken()
                if (token != null) {
                    val createPostRequest = CreatePostRequest(title, description, category, 1111010100, price, deposit, condition, 1721270024, 1721270024)

                    // Retrofit 요청에 토큰을 포함시키는 코드 추가
                    val client = RetrofitClient.instance
                    val call = client.createPost(createPostRequest).clone() // 필요시 헤더 추가 후 호출 가능

                    call.enqueue(object : retrofit2.Callback<CreatePostResponse> {
                        override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {
                            if (response.isSuccessful) {
                                val postId = response.body()?.data?.createdPostId
                                if (postId != null) {
                                    uploadImagesToServer(postId)
                                } else {
                                    Log.e("PostAddFragment", "게시글 생성 후 postId를 받아오지 못했습니다.")
                                }
                                // 성공 처리
                                (context as MainActivity).supportFragmentManager.beginTransaction()
                                    .replace(R.id.container, HomeFragment())
                                    .commitAllowingStateLoss()
                                showToast("게시글이 업로드 되었습니다.")
                                Log.d("PostAddFragment", "Request : ${createPostRequest}")
                                Log.d("PostAddFragment", "Response : ${response.body()}")
                            } else {
                                Log.e("PostAddFragment", "게시글 업로드 실패 - 오류: ${response.code()} - ${response.message()}")
                                showToast("게시글 업로드에 실패했습니다.")
                            }
                        }

                        override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
                            t.printStackTrace()
                            showToast("네트워크 오류가 발생했습니다.")
                        }
                    })
                } else {
                    showToast("로그인이 필요합니다.")
                }
            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        refreshData()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // BottomNavigationView 다시 보이기
        (activity as? MainActivity)?.showBottomNavigation()
        _binding = null
    }
    private fun getToken(): String? {
        val sharedPreferences = context?.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("TOKEN", null)
    }

    private fun showToast(message: String) {
        val encodedMessage = String(message.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        Toast.makeText(activity, encodedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun refreshData() {
        val postAddService = PostAddService()
        postAddService.setPostAddView(this)
        postAddService.getCategories(requireContext())
    }

    override fun onGetCategoriesSuccess(result: GetCategoryManifestResponse) {
        postAddCategoryRVAdapter.updateData(result)
        postAddCategoryRVAdapter.setPostCategoryClickListener(object : PostAddCategoryRVAdapter.PostCategoryItemClickListener {
            override fun onItemClick(name: String) {
                //
            }
        })
        Log.d("PostAddFragment", "Get Categories Success")
    }

    override fun onGetCategoriesFailure(message: String) {
        Log.d("PostAddFragment", "Get Categories Failure - ${message}")
    }

    private fun setupRecyclerViews() {
        binding.postAddCategoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postAddCategoryRVAdapter = PostAddCategoryRVAdapter(GetCategoryManifestResponse(emptyList()), this)
        binding.postAddCategoryRv.adapter = postAddCategoryRVAdapter

        binding.postAddConditionRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postAddConditionRVAdapter = PostAddConditionRVAdapter(conditionsKor, this)
        binding.postAddConditionRv.adapter = postAddConditionRVAdapter

        binding.postAddPhotoRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        photoAdapter = PostAddPhotoRVAdapter(requireContext(), photoList, object : PostAddPhotoRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == 0 && photoList[0].toString().contains("img_post_add_photo")) {
                    openGallery() // 기본 이미지가 클릭되면 갤러리 열기
                }
            }

            override fun onDeleteClick(position: Int) {
                if (position != 0) { // 기본 이미지가 아닌 경우에만 삭제
                    photoList.removeAt(position)
                    photoAdapter.notifyItemRemoved(position)
                    photoAdapter.notifyItemRangeChanged(position, photoList.size)
                }
            }
        })
        binding.postAddPhotoRv.adapter = photoAdapter
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                // 선택된 이미지를 기본 이미지 다음에 추가
                photoList.add(it)
                photoAdapter.notifyItemInserted(photoList.size - 1)
            }
        }
    }

    private fun uploadImagesToServer(postId: Int) {
        // 전송할 이미지 리스트 필터링 (기본 이미지는 제외)
        val serverImageList = photoList.filter { !it.toString().contains("img_post_add_photo") }

        // 전송할 이미지가 있는지 확인
        if (serverImageList.isNotEmpty()) {
            // 첫 번째 이미지 전송 시작
            uploadImage(postId, serverImageList, 0)
        } else {
            Log.e("PostAddFragment", "전송할 이미지가 없습니다.")
        }
    }

    private fun uploadImage(postId: Int, imageList: List<Uri>, index: Int) {
        // 모든 이미지를 전송했으면 종료
        if (index >= imageList.size) {
            Log.d("PostAddFragment", "모든 이미지를 성공적으로 전송했습니다.")
            return
        }

        val uri = imageList[index]
        val file = File(getRealPathFromURI(uri))
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val call = RetrofitClient.instance.uploadPostImage(postId, body)
        call.enqueue(object : Callback<UploadPostImageResponse> {
            override fun onResponse(call: Call<UploadPostImageResponse>, response: Response<UploadPostImageResponse>) {
                if (response.isSuccessful) {
                    Log.d("PostAddFragment", "이미지 전송 성공: ${file.name}")
                    // 다음 이미지를 전송
                    uploadImage(postId, imageList, index + 1)
                } else {
                    Log.e("PostAddFragment", "이미지 전송 실패 - 오류: ${response.code()} - ${response.message()}")
                    showToast("이미지를 전송하지 못했습니다.")
                }
            }

            override fun onFailure(call: Call<UploadPostImageResponse>, t: Throwable) {
                Log.e("PostAddFragment", "이미지 전송 실패: ${t.localizedMessage}")
                showToast("네트워크 오류가 발생했습니다.")
            }
        })
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var result = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }

}