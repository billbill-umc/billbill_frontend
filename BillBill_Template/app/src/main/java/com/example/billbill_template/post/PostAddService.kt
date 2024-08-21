package com.example.billbill_template.post


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.billbill_template.Login.signup.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class PostAddService {

    private lateinit var postAddView: PostAddView

    fun setPostAddView(postAddView: PostAddView) {
        this.postAddView = postAddView
    }

    fun getCategories(context: Context) {
        RetrofitClient.instance.getCategoryManifest().enqueue(object : retrofit2.Callback<GetCategoryManifestResponse> {
            override fun onResponse(
                call: Call<GetCategoryManifestResponse>,
                response: Response<GetCategoryManifestResponse>
            ) {
                Log.d("HomeService", "Response: ${response.body()}")
                Log.d("HomeService", "Raw Response: ${response.raw()}")
                if (response.isSuccessful && response.body() != null) { // Null 체크 추가
                    val getCategoryResponse: GetCategoryManifestResponse = response.body()!!
                    Log.d("HomeService", getCategoryResponse.toString())
                    postAddView.onGetCategoriesSuccess(getCategoryResponse)
                } else {
                    Log.e("HomeService", "카테고리 목록 불러오기 실패 - 오류: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "카데고리 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetCategoryManifestResponse>, t: Throwable) {
                Log.e("HomeService", "getCategories onFailure: ${t.localizedMessage}")
                t.printStackTrace()
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }
}