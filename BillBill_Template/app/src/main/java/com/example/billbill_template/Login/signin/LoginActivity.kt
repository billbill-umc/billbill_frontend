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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // FragmentLoginBinding 사용하여 레이아웃 설정
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // signup_new_btn 클릭 리스너 설정
        binding.signupNewBtn.setOnClickListener {
            // SignUpActivity로 이동
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtnLogin.setOnClickListener {
            val email = binding.loginInputId.text.toString()
            val password = binding.loginInputPw.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)
                RetrofitClient.instance.getToken(loginRequest).enqueue(object : retrofit2.Callback<TokenResponse> {
                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                        Log.d("LoginActivity", "Response: ${response.body()}")

                        if (response.isSuccessful && response.body()?.data?.accessToken != null) {
                            val token = response.body()?.data?.accessToken
                            Log.d("LoginActivity", "Token received: $token")

                            val sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("TOKEN", token)
                            editor.apply()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("LoginActivity", "Login failed: ${response.code()} - ${response.message()}")
                            Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }



                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(this@LoginActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
