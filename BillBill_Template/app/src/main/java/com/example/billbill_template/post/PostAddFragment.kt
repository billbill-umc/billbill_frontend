<<<<<<< HEAD
import android.app.DatePickerDialog
import android.content.Context
=======
package com.example.billbill_template.post

import android.app.DatePickerDialog
>>>>>>> origin/main
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostAddBinding
<<<<<<< HEAD
import com.example.billbill_template.post.CreatePostRequest
import com.example.billbill_template.post.CreatePostResponse
=======
>>>>>>> origin/main
import com.example.billbill_template.ui.home.HomeFragment
import retrofit2.Call
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.util.Calendar

class PostAddFragment : Fragment() {
<<<<<<< HEAD
    private var _binding: FragmentPostAddBinding? = null
=======
    private var _binding : FragmentPostAddBinding? = null
>>>>>>> origin/main
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

<<<<<<< HEAD
=======

>>>>>>> origin/main
        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        binding.postAddBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitAllowingStateLoss()
        }

<<<<<<< HEAD
        binding.postAddInputCalendarIv.setOnClickListener{
            // 캘린더 선택 부분 (주석 처리된 부분) - 필요시 구현
=======
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
>>>>>>> origin/main
        }

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

<<<<<<< HEAD
                if(depositString.isNotEmpty()) {
                    deposit = depositString.toInt()
                }

                val token = getToken()
                if (token != null) {
                    val createPostRequest = CreatePostRequest(title, description, 1, 1, price, deposit, "", 1, 1)

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
=======
                if(depositString != "") { var deposit = depositString.toInt() }

                val createPostRequest =
                    CreatePostRequest(title, description, 1, 1, price, deposit, "", 1, 1)
                RetrofitClient.instance.createPost(createPostRequest).enqueue(object : retrofit2.Callback<CreatePostResponse> {
                    override fun onResponse(
                        call: Call<CreatePostResponse>,
                        response: Response<CreatePostResponse>
                    ) {
                        Log.d("PostAddFragment", "Response: ${response.body()}")

                        if (response.isSuccessful) {
                            //홈 화면으로 돌아가기
                            (context as MainActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.container, HomeFragment())
                                .commitAllowingStateLoss()
                            showToast("게시글이 업로드 되었습니다.")
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
>>>>>>> origin/main
            }
        }

        return root
    }
<<<<<<< HEAD

=======
>>>>>>> origin/main
    override fun onDestroyView() {
        super.onDestroyView()
        // BottomNavigationView 다시 보이기
        (activity as? MainActivity)?.showBottomNavigation()
        _binding = null
    }
<<<<<<< HEAD

    private fun getToken(): String? {
        val sharedPreferences = context?.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("TOKEN", null)
    }

    private fun showToast(message: String) {
        val encodedMessage = String(message.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        Toast.makeText(activity, encodedMessage, Toast.LENGTH_SHORT).show()
    }
}
=======
    // 깨진 문자를 방지하기 위해 UTF-8로 인코딩된 문자열로 Toast 메시지를 표시합니다.
    private fun showToast(message: String) {
        val encodedMessage = String(message.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        Toast.makeText(getActivity(), encodedMessage, Toast.LENGTH_SHORT).show()
    }
}
>>>>>>> origin/main
