package com.example.billbill_template.ui.message

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.billbill_template.Login.signup.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class MessageService() {
    private lateinit var messageView: MessageView

    fun setMessageView(messageView: MessageFragment) {
        this.messageView = messageView
    }

    fun getMessages(context: Context) {
        RetrofitClient.instance.getChattings().enqueue(object : retrofit2.Callback<GetChattingsResponse> {
            override fun onResponse(
                call: Call<GetChattingsResponse>,
                response: Response<GetChattingsResponse>
            ) {
                Log.d("MessageService", "Response: ${response.body()}")
                if (response.isSuccessful) {
                    val getChattingsResponse: GetChattingsResponse = response.body()!!
                    Log.d("MessageService", getChattingsResponse.toString())
                    messageView.onGetMessagesSuccess(getChattingsResponse.data!!)
                } else {
                    Log.e("HomeService", "채팅방 목록 불러오기 실패 - 오류: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "채팅방 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetChattingsResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}