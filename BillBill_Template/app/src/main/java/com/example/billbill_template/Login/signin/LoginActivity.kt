package com.example.billbill_template.Login.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billbill_template.Login.signup.LoginRequest
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.Login.signup.SignUpActivity
import com.example.billbill_template.MainActivity
import com.example.billbill_template.databinding.ActivityLoginBinding
import com.example.billbill_template.Login.signup.TokenResponse
import com.example.billbill_template.ui.mypage.UserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitClient.initialize(this)

        binding.signupNewBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtnLogin.setOnClickListener {
            val email = binding.loginInputId.text.toString()
            val password = binding.loginInputPw.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)
                RetrofitClient.instance.getToken(loginRequest).enqueue(object : Callback<TokenResponse> {
                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                        if (response.isSuccessful && response.body()?.data?.accessToken != null) {
                            val token = response.body()?.data?.accessToken
                            val sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("TOKEN", token)
                            editor.apply()

                            // 사용자 정보를 별도로 요청하여 저장
                            fetchUserInfoAndSave()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserInfoAndSave() {
        RetrofitClient.instance.getUserInfo().enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userInfo = response.body()!!
                    val sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("USERNAME", userInfo.username)
                        putString("AVATARURL", userInfo.avatarUrl)
                        apply()
                    }
                } else {
                    Log.e("LoginActivity", "Failed to fetch user info: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error fetching user info", t)
            }
        })
    }
}
