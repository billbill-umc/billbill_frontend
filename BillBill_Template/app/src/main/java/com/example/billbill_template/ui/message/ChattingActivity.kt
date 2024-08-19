package com.example.billbill_template.ui.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billbill_template.Login.signin.LoginActivity
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.databinding.ActivityChattingBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        val chattingId = intent.getIntExtra("chatId", -1)
        if (chattingId != -1) {
            loadChattingDetails(chattingId)
        } else {
            Toast.makeText(this, "잘못된 채팅방 ID 입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // MainActivity를 종료하여 뒤로가기 버튼으로 돌아오지 않도록 함
            return
        }

        //Socket.io기반 실시간 채팅 구현

        // Socket 초기화
        SocketManager.initializeSocket(chattingId.toString(), token)
        socket = SocketManager.getSocket()

        // 소켓 연결
        socket.connect()

        // NEW_MESSAGE 이벤트 리스너 등록
        socket.on("NEW_MESSAGE", onNewMessage)

        // 버튼 클릭 시 메시지 전송
        binding.chattingInputSend.setOnClickListener {
            val message = binding.chattingInputTextEt.text.toString()
            if (message.isNotEmpty()) {
//                sendMessage(message)
                val messageObject = JSONObject().apply {
                    put("type", "TEXT") // 현재 TEXT 타입만 지원
                    put("content", message) // 메시지 내용
                    put("sentAt", System.currentTimeMillis() / 1000L) // Unix 타임스탬프 (초 단위)
                }
                socket.emit("NEW_MESSAGE", messageObject)
                Log.d("ChattingActivity", "채팅을 전송합니다. object : ${messageObject}")
                binding.chattingInputTextEt.text = null
            }
        }

        chattingClickListener()

        setContentView(binding.root)
    }

    private fun loadChattingDetails(chattingId: Int) {
        RetrofitClient.instance.getChattingById(chattingId)
            .enqueue(object : Callback<GetChattingByIdResponse> {
                override fun onResponse(
                    call: Call<GetChattingByIdResponse>,
                    response: Response<GetChattingByIdResponse>
                ) {
                    Log.d("ChattingActivity", "Response: ${response.body()}")
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("ChattingActivity", "채팅방 불러오기 성공 : ${response.body()}")
                        val chattingDetail: GetChattingByIdData? = response.body()!!.data
                        binding.chattingObjectNameTv.text = chattingDetail?.post!!.itemName
                        binding.chattingObjectCostTv.text = "${chattingDetail.post.price}원"
                        binding.chattingTitleUserTv.text = chattingDetail?.to?.username
                    } else {
                        Log.e("ChattingActivity", "채팅방 불러오기 실패 - 오류: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GetChattingByIdResponse>, t: Throwable) {
                    Log.e("ChattingActivity", "getChattingByIdResponse onFailure: ${t.localizedMessage}")
                    t.printStackTrace()
                }
            })
    }

    private fun chattingClickListener() {
        binding.chattingBackIv.setOnClickListener {
            finish()
        }
    }

//    private fun sendMessage(content: String) {
//        val messageObject = JSONObject().apply {
//            put("type", "TEXT") // 현재 TEXT 타입만 지원
//            put("content", content) // 메시지 내용
//            put("sentAt", System.currentTimeMillis() / 1000L) // Unix 타임스탬프 (초 단위)
//        }
//        socket.emit("NEW_MESSAGE", messageObject)
//        Log.d("ChattingActivity", "채팅을 전송합니다. object : ${messageObject}")
//
//    }

    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            val data = args[0] as JSONObject
            val messageContent = data.getString("content")
            // 메시지를 UI에 표시
//            chatTextView.append("\n$messageContent") todo
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        socket.off("NEW_MESSAGE", onNewMessage)
    }
}
