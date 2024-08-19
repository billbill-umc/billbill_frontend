package com.example.billbill_template.post

import android.content.Context
import android.os.Bundle
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
import com.example.billbill_template.ui.home.HomeCategoryRVAdapter
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.home.HomePostRVAdapter
import retrofit2.Call
import retrofit2.Response
import java.nio.charset.StandardCharsets

class PostAddFragment : Fragment(), PostAddView {
    private var _binding: FragmentPostAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var postCategoryRVAdapter: PostCategoryRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostAddBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        binding.postAddBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()).commitAllowingStateLoss()
        }
//        binding.postAddPhotoRv.setOnClickListener {  }

        binding.postAddInputCalendarIv.setOnClickListener{
//            var calendar = Calendar.getInstance()
//            var year = calendar.get(Calendar.YEAR)
//            var month = calendar.get(Calendar.MONTH)
//            var day = calendar.get(Calendar.DAY_OF_MONTH)
//            context?.let { it1 ->
//                DatePickerDialog(it1, { _, year, month, day ->
//                    run {
//                        binding.postAddCalendarResultTv.setText(year.toString() + "." + (month + 1).toString() + "." + day.toString())
//                    }
//                }, year, month, day)
//            }?.show()

//            val cal = Calendar.getInstance()
//            val data = DatePickerDialog.OnDateSetListener {
//                view, year, month, day ->
//                binding.postAddCalendarResultTv.text = "${year}.${month}.${day}"
//            }
//            DatePickerDialog(this, data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
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

                if(depositString.isNotEmpty()) {
                    deposit = depositString.toInt()
                }

                val token = getToken()
                if (token != null) {
                    val createPostRequest = CreatePostRequest(title, description, 1, 1111010100, price, deposit, "NEW", 1721270024, 1721270024)

                    // Retrofit 요청에 토큰을 포함시키는 코드 추가
                    val client = RetrofitClient.instance
                    val call = client.createPost(createPostRequest).clone() // 필요시 헤더 추가 후 호출 가능

                    call.enqueue(object : retrofit2.Callback<CreatePostResponse> {
                        override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {
                            if (response.isSuccessful) {
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

    override fun onGetCategoriesSuccess(result: GetCategoryManifestResponse) {
        TODO("Not yet implemented")
    }

    override fun onGetCategoriesFailure(message: String) {
        TODO("Not yet implemented")
    }

    private fun setupRecyclerViews() {

        binding.postAddCategoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postCategoryRVAdapter = PostCategoryRVAdapter(GetCategoryManifestResponse(emptyList()), this)
        binding.postAddCategoryRv.adapter = postCategoryRVAdapter
    }
}